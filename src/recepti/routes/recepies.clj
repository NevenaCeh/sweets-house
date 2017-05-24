(ns recepti.routes.recepies
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
  )
  (:import [java.io File FileInputStream FileOutputStream]
[java.awt.image AffineTransformOp BufferedImage]
java.awt.RenderingHints
java.awt.geom.AffineTransform
javax.imageio.ImageIO)
)

(defn authenticated [session]
  (authenticated? session))

(defn get-page-of-all-recepies [{:keys [params session] request :request} &[poruka]]
  (render-file "templates/recepti.html" {:recepti (db/vrati-recepte) :user (:identity session) :authenticated (str (authenticated session)) :poruka poruka}))

(defn get-page-add-recipe [{:keys [params session] request :request}]
  (render-file "templates/novirecept.html" {:user (:identity session) :authenticated (str (authenticated session))}))

(defn handle-add-recipe [{:keys [params session] request :request}]
  (let [naziv (:naziv params)
        sastojci (:sastojci params)
        opis (:opis params)
        napisano (new java.util.Date)
        slika (str "/slike recepata/" (:filename (:slika params)))
        receptod (:receptod params)
        dozvoljeno false
        ]
      (db/dodaj-recept naziv sastojci opis slika napisano receptod dozvoljeno)
        ;(redirect "/recepti" )
     (assoc (redirect "/recepti") :poruka "Cekamo vest od administratora!")
    ))

(defn get-page-of-recipe [{:keys [params session] request :request}]
  (println (:identity session))
  (render-file "templates/recept-prikaz.html"
               {:recept (first (db/vrati-recept-id (:receptID params)))
                :user (:identity session)
                :authenticated (str (authenticated session))}))

(defroutes recepti-routes
  (GET "/recepti" request (get-page-of-all-recepies request))
  (GET "/addrecipe" request (get-page-add-recipe request))
  (POST "/addrecipe" request (handle-add-recipe request))
  (GET "/vidirecept/:id" request (get-page-of-recipe request))
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
	(str "error uploading file " (.getMessage ex)))))))

{params :params} (upload-file (get params "file"))
)
