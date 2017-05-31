(ns recepti.routes.recepies
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.java.io :as io]
            [noir.io :refer [upload-file resource-path]]
            [hiccup.element :refer [image]]
            [hiccup.util :refer [url-encode]]
            ;;[clojure.contrib [duck-streams :as ds]]
  )
    (:import [java.io File FileInputStream FileOutputStream]
             [java.awt.image AffineTransformOp BufferedImage]
              java.awt.RenderingHints
              java.awt.geom.AffineTransform
              javax.imageio.ImageIO))

(defn authenticated [session]
  (authenticated? session))

(defn gallery-path []
      "slike")

(def thumb-size 150)

(def thumb-prefix "thumb_")

(defn scale [img ratio width height]
  (let [scale (AffineTransform/getScaleInstance
  (double ratio) (double ratio))
  transform-op (AffineTransformOp.
  scale AffineTransformOp/TYPE_BILINEAR)]
  (.filter transform-op img (BufferedImage. width height (.getType img)))))

(defn scale-image [file]
  (let [img (ImageIO/read file)
  img-width (.getWidth img)
  img-height (.getHeight img)
  ratio (/ thumb-size img-height)]
  (scale img ratio (int (* img-width ratio)) thumb-size)))

(defn save-thumbnail [{:keys [filename]}]
  (let [path (str (gallery-path) File/separator)]
  (ImageIO/write
  (scale-image (io/input-stream (str path filename)))
  "jpeg"
  (File. (str path thumb-prefix filename)))))

(defn get-page-of-all-recepies [{:keys [params session] request :request} &[poruka]]
  (render-file "templates/recepti.html" {:recepti (db/vrati-recepte) :user (:identity session) :authenticated (str (authenticated session)) :poruka poruka}))

(defn get-page-add-recipe [{:keys [params session] request :request}]
  (render-file "templates/novirecept.html" {:user (:identity session) :authenticated (str (authenticated session))}))

(defn upload-picture [file]
  (println "U metodi sam za upload")
    (noir.io/upload-file (gallery-path) file)
)

(defn handle-add-recipe [{:keys [params session] request :request}]
  (upload-picture (:slika params))
  (println params)
  (let [naziv (:naziv params)
        sastojci (:sastojci params)
        opis (:opis params)
        napisano (new java.util.Date)
        slika (str "/slike recepata/" (:filename (:file params)))
        receptod (:receptod params)
        dozvoljeno false
        ]
      (db/dodaj-recept naziv sastojci opis slika napisano receptod dozvoljeno)
        ;(redirect "/recepti" )
     (assoc (redirect "/recepti") :poruka "Cekamo vest od administratora!")
    ))

;(defn get-page-of-recipe [{:keys [params session] request :request}]
 ; (println (:identity session))
  ;;(println (first (db/vrati-recept-id (:id params))))
 ; (render-file "templates/recept-prikaz.html"
      ;         {:recept (first (db/vrati-recept-id (:id params)))
   ;             :user (:identity session)
       ;         :authenticated (str (authenticated session))}))

(defn get-page-of-recipe [{:keys [params session] request :request}]
  (println params)
  (render-file "templates/recept-prikaz.html"
               {:recept (first (db/vrati-recept-id (:id params)))
                :lajkovi (count(db/lajkovi-za-recept (:id params)))
                :ovajlajkovao (count(db/ovaj-user-lajkovao (:id params :username (:identity session))))
                :idtoglajka (first(db/ovaj-user-lajkovao (:id params :username (:identity session))))
                :komentari (db/komentari-za-recept (:id params))
                :user (:identity session)
                :authenticated (str (authenticated session))}))

(defn delete-recepy [{:keys [params session] request :request}]
  (println "Udje u metodu")
  (println params)
  (db/obrisi-recept (:id params))
  (redirect "/svirecepti"))

(defn add-like-to-recipe [{:keys [params session] request :request}]
  (let [ostavio (:lajkovao params)
        receptid (:id params)
        ]
      (db/dodaj-lajk-na-recept receptid ostavio)
        ;(redirect "/recepti" )
     (redirect (str "/vidirecept/" receptid))
    ))

(defn dislike-to-recipe [{:keys [params session] request :request}]
  (let [lajkid (:id params)
        receptid (:recept params )
        ]
      (db/obrisi-lajk-sa-recepta lajkid)
        ;(redirect "/recepti" )
     (redirect (str "/vidirecept/" receptid))
    ))


(defroutes recepti-routes
  (GET "/recepti" request (get-page-of-all-recepies request))
  (GET "/addrecipe" request (get-page-add-recipe request))
  (POST "/addrecipe" request (handle-add-recipe request))
  (GET "/vidirecept/:id" request (get-page-of-recipe request))
  (POST "/dodajlajk/:id" request (add-like-to-recipe request))
  (POST "/obrisilajk/:id" request (dislike-to-recipe request))
  ;;(GET "/obrisirecept/:id" request (delete-recepy request))
)

( comment
(defn handle-upload [{:keys [filename] :as file}]
	(upload-page
	(if (empty? filename)
	"please select a file to upload"
	(try
	(upload-file (gallery-path) file)
	(save-thumbnail file)
	(db/add-image (session/get :user) filename)
	(image {:height "150px"}
	(thumb-uri (session/get :user) filename))
	(catch Exception ex
	(str "error uploading file " (.getMessage ex))))
    )))

{params :params} (upload-file (get params "file"))
)
