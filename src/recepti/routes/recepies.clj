(ns recepti.routes.recepies
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :refer [response redirect content-type]]
            [clojure.java.io :as io]
            [hiccup.form :refer :all]
            [hiccup.element :refer [image]]
            [hiccup.util :refer [url-encode]]
            [noir.io :refer [upload-file resource-path]]
  )
    (:import [java.io File FileInputStream FileOutputStream]
             [java.awt.image AffineTransformOp BufferedImage]
              java.awt.RenderingHints
              java.awt.geom.AffineTransform
              javax.imageio.ImageIO))

(defn authenticated [session]
  (authenticated? session))

(def galleries "/img/recepti")

(defn gallery-path []
(str "resources/public" galleries))

(def thumb-size 300)

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

(defn save-thumbnail [file ime]
  (let [
         path (str (gallery-path) File/separator)
       ]
  (ImageIO/write
  (scale-image (io/input-stream (str path (:filename file))))
  "jpeg"
  (File. (str path thumb-prefix ime ".jpg")))))

(defn get-page-of-all-recepies [{:keys [params session] request :request} &[poruka]]
 (if-not (authenticated? session)
  (redirect "/login")
  (render-file "templates/recepti.html" {:recepti (db/vrati-recepte) :user (:identity session) :authenticated (str (authenticated session))})))

(defn get-page-add-recipe [{:keys [params session] request :request}]
  (render-file "templates/novirecept.html" {:user (:identity session) :authenticated (str (authenticated session))}))

(defn upload-picture [file ime]
(try
(noir.io/upload-file (gallery-path) file :create-path? true)
(save-thumbnail file ime)
(image {:height "150px"}
(str "/img/" thumb-prefix (url-encode (:filename file))))
(catch Exception ex
(str "error uploading file " (.getMessage ex)))))

(defn handle-add-recipe [{:keys [params session] request :request}]
  (upload-picture (:file params) (:naziv params)) ;radi u chromu, ne u exploreru
  (let [naziv (:naziv params)
        sastojci (:sastojci params)
        opis (:opis params)
        napisano (new java.util.Date)
        slika (str galleries "/" thumb-prefix naziv ".jpg")
        receptod (:receptod params)
        dozvoljeno false
        kratko (:kratko params)
        ]
   (println slika)
     (db/dodaj-recept naziv sastojci opis slika napisano receptod dozvoljeno kratko)
     (assoc (redirect "/recepti") :poruka "Cekamo vest od administratora!")))

(defn lajkovaouser [id user]
  (println (some? (first (db/ovaj-user-lajkovao id user))))
  (some? (first (db/ovaj-user-lajkovao id user))))

(defn get-page-of-recipe [{:keys [params session] request :request}]
  (if-not (authenticated? session)
  (redirect "/login")
  (let [
         recept (first (db/vrati-recept-id (:id params)))
         lajkovi (count(db/lajkovi-za-recept (:id params)))
         on (str (lajkovaouser (:id params) (:username (:identity session))))
         idlajka (first(db/ovaj-user-lajkovao (:id params) (:username (:identity session))))
         komentari (db/komentari-za-recept (:id params))
      ]
    (render-file "templates/recept-prikaz.html"
               {:recept recept
                :lajkovi lajkovi
                :ovajlajkovao on
                :idtoglajka idlajka
                :komentari komentari
                :user (:identity session)
                :authenticated (str (authenticated session))}))))

(defn delete-recepy [{:keys [params session] request :request}]
  (db/obrisi-komentare-za-recept (:id params))
  (db/obrisi-lajkove-za-recept (:id params))
  (db/obrisi-recept (:id params))
  (redirect "/svirecepti"))

(defn add-like-to-recipe [{:keys [params session] request :request}]
  (let [ostavio (:lajkovao params)
        receptid (:id params)
        ]
      (db/dodaj-lajk-na-recept receptid ostavio)
     (redirect (str "/vidirecept/" receptid))))

(defn dislike-to-recipe [{:keys [params session] request :request}]
  (let [lajkid (:id params)
        receptid (:recept params )
        ]
      (db/obrisi-lajk-sa-recepta lajkid)
     (redirect (str "/vidirecept/" receptid))))

(defn add-comment-to-recipe [{:keys [params session] request :request}]
  (if-not (authenticated? session)
  (redirect "/login")
  (let [ostavio (:username (:identity session))
        receptid (:id params)
        tekst (:opis params)
        datum (new java.util.Date)
        ]
      (db/dodaj-komentar-na-recept tekst ostavio datum receptid)
     (redirect (str "/vidirecept/" receptid)))))


(defroutes recepti-routes
  (GET "/recepti" request (get-page-of-all-recepies request))
  (GET "/addrecipe" request (get-page-add-recipe request))
  (POST "/addrecipe" request (handle-add-recipe request))
  (GET "/vidirecept/:id" request (get-page-of-recipe request))
  (POST "/dodajlajk/:id" request (add-like-to-recipe request))
  (POST "/obrisilajk/:id" request (dislike-to-recipe request))
  (POST "/dodajkomentar/:id" request (add-comment-to-recipe request))
  (GET "/obrisirecept/:id" request (delete-recepy request))
)

