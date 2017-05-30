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
  ;(cond
  ;  (not (authenticated session))
    ; (redirect "/login")
  ;  :else
     (render-file "templates/admin.html"
                 {:admin (:identity session)})
  ;)
)

(defn get-all-impressions-page [{:keys [params session] request :request}]
  (render-file "templates/admin-utisci.html" {:utisci (db/vrati-utiske) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn get-all-recepies-page [{:keys [params session] request :request}]
  (render-file "templates/admin-recepti.html" {:recepti (db/vrati-sve-recepte) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn get-forbiden-recipies [{:keys [params session] request :request}]
  (render-file "templates/admin-recepti.html" {:recepti (db/vrati-zabranjene-recepte) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn get-free-recipies [{:keys [params session] request :request}]
  (render-file "templates/admin-recepti.html" {:recepti (db/vrati-recepte) :admin (:identity session) :authenticated (str (authenticated session))}))

(defn delete-impression [{:keys [params session] request :request}]
  (println params)
  (db/obrisi-utisak (:id params))
  (redirect "/sviutisci"))

(defn delete-recepy [{:keys [params session] request :request}]
  (println "Udje u metodu")
  (println params)
  (db/obrisi-recept (:id params))
  (redirect "/svirecepti"))

(defn edit-receipt [{:keys [params session] request :request}]
  (println params)
  (db/dozvoli-recept (:id params))
  (redirect "/svirecepti"))

(defn forbiden-receipt [{:keys [params session] request :request}]
  (println params)
  (db/zabrani-recept (:id params))
  (redirect "/svirecepti"))

(defn get-chosen-recepy-page [{:keys [params session] request :request}]
  (render-file "templates/admrecept.html" {:recept (first (db/vrati-recept-id (:id params)))
                                                         :admin (:identity session)
                                                         :authenticated (str (authenticated session))}))

(defroutes administracija-routes
  (GET "/admin" request (get-admin-page request))
  (GET "/sviutisci" request (get-all-impressions-page request))
  (GET "/svirecepti" request (get-all-recepies-page request))
  (GET "/obrisiutisak/:id" request (delete-impression request))
  (GET "/dozvoli/:id" request (edit-receipt request))
  (GET "/zabrani/:id" request (forbiden-receipt request))
  ;;(GET "/obrisirecept/:id" request (delete-recepy request))
  (GET "/pogledaj/:id" request (get-chosen-recepy-page request))
  (POST "/nedozvoljeni" request (get-forbiden-recipies request))
  (POST "/dozvoljeni" request (get-free-recipies request))
  (POST "/svirecepti" request (get-all-recepies-page request))
)
