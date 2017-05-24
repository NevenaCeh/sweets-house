(ns recepti.routes.administracija
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
  )
)

(defn authenticated [session]
  (authenticated? session))

(defn get-admin-page [session]
  ;;(cond
    ;;(not (authenticated session))
     ;;(redirect "/adminlogin")
    ;;:else
     (render-file "templates/admin.html"
                 {:title "Admin-Home"
                  :admin (:identity session)
                 })
  ;;)
)

(defn get-all-impressions-page [{:keys [params session] request :request}]
  (render-file "templates/admin-utisci.html" {:utisci (db/vrati-utiske) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn get-all-recepies-page [{:keys [params session] request :request}]
  (render-file "templates/admin-recepti.html" {:recepti (db/vrati-sve-recepte) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn delete-impression [{:keys [params session] request :request}]
  (println params)
  (db/obrisi-utisak (:id params))
  (redirect "/sviutisci"))

(defn edit-receipt [{:keys [params session] request :request}]
  (println params)
  (db/dozvoli-recept (:id params))
  (redirect "/svirecepti"))

(defroutes administracija-routes
  (GET "/admin" request (get-admin-page request))
  (GET "/sviutisci" request (get-all-impressions-page request))
  (GET "/svirecepti" request (get-all-recepies-page request))
  (GET "/obrisiutisak/:id" request (delete-impression request))
  (GET "/dozvoli/:id" request (edit-receipt request))
)
