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
  (render-file "templates/admin-utisci.html" {:utisci (db/vrati-utiske) :admin (:identity session)}))

(defn delete-impression [{:keys [params session] request :request}]
  (println params)
  (db/obrisi-utisak (:id params))
  (redirect "/sviutisci"))

(defroutes administracija-routes
  (GET "/admin" request (get-admin-page request))
  (GET "/sviutisci" request (get-all-impressions-page request))
  (GET "/obrisiutisak/:id" request (delete-impression request))
)
