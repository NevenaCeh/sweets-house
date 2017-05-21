(ns recepti.routes.recepies
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
  )
)

(def galleries "galleries")


;;(defn gallery-path []
;;(str galleries File/separator (session/get :user)))

;(defn save-thumbnail [{:keys [filename]}]
;(let [path (str (gallery-path) File/separator)]
;(ImageIO/write
;(scale-image (io/input-stream (str path filename)))
;"jpeg"
;(File. (str path thumb-prefix filename)))))

(defn get-page-of-all-recepies [{:keys [params session] request :request}]
  (render-file "templates/recepti.html" {:recepti (db/vrati-recepte) :user (:identity session)}))

(defn get-page-add-recipe [{:keys [params session] request :request}]
  (render-file "templates/novirecept.html" {:user (:identity session)}))

(defn handle-add-recipe [{:keys [params session] request :request} slika]
  (db/dodaj-recept params :napisano (new java.util.Date))
  (redirect "/addrecipe")
)

(defroutes recepti-routes
  (GET "/recepti" request (get-page-of-all-recepies request))
  (GET "/addrecipe" request (get-page-add-recipe request))
  (POST "/addrecipe" request (handle-add-recipe request))
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
