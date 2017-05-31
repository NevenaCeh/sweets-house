(ns recepti.routes.utisci
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
  )
)

(defn authenticated [session]
  (authenticated? session))

(defn get-logged-username [session]
  (:id (:identity session)))

(defn get-page-of-all-impressions [{:keys [params session] request :request}]
  (render-file "templates/utisci.html" {:utisci (db/vrati-utiske) :user (:identity session)}))

(defn get-page-add-impression [{:keys [params session] request :request}]
  (render-file "templates/dodajutisak.html" {:user (:identity session)}))

(defn handle-add-impression [{:keys [params session] request :request}]
  (let [naziv (:naziv params)
        opis (:opis params)
        ostavio (:ostavio params)
        datum (new java.util.Date)]
    (println ostavio)
      (db/dodaj-utisak naziv opis ostavio datum)
        (redirect "/knjigautisaka"))
)


(defn delete-own-impression [{:keys [params session] request :request}]
  (db/obrisi-utisak (:id params))
  (redirect "/knjigautisaka"))

(defn edit-own-impression-get-page [{:keys [params session] request :request}]
      (render-file "templates/dodajutisak.html" {:user (:identity session) :utisak (first (db/vrati-utisak (:id params))) :authenticated (str (authenticated session))})
)

(defn handle-edit-own-impression [{:keys [params session] request :request}]
  (let [naziv (:naziv params)
        opis (:opis params)
        ]
      (db/izmeni-utisak (:id params) naziv opis)
        (redirect "/knjigautisaka"))
)

(defroutes utisci-routes
  (GET "/knjigautisaka" request (get-page-of-all-impressions request))
  (GET "/addutisak" request (get-page-add-impression request))
  (POST "/addutisak" request (handle-add-impression request))
  (GET "/obrisisvoj/:id" request (delete-own-impression request))
  (GET "/izmenisvoj/:id" request (edit-own-impression-get-page request))
  (POST "/izmenisvoj/:id" request (handle-edit-own-impression request))
)
