(ns recepti.routes.auth
  (:require [compojure.core :refer :all]
            [recepti.views.layout :as layout]
            [selmer.parser :refer [render-file]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :refer [response redirect content-type]]
            [recepti.models.db :as db]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [clojure.string :refer [blank?]]
  )
)

(defn get-login-page []
  (render-file "templates/login.html" {}))

(defn handle-login [{:keys [params session] request :request}]
  (let [user (first (db/get-user (:username params) (:password params)))]
    (println (some? user))
    (if (some? user)
      (assoc (redirect "/"):session (assoc session :identity user))
      (render-file "templates/login.html" {:error "Wrong username or password"}))))

(defn logout
  [request]
  (-> (redirect "/login")
      (assoc :session {})))

(defn get-registration-page []
  (render-file "templates/register.html" {}))

(defn handle-registration [{:keys [params session] request :request}]
  (assoc (redirect "/"):session (assoc session :identity "")))


(defroutes auth-routes
  (GET "/login" [] (get-login-page))
  (POST "/login" request (handle-login request))
  (GET "/logout" request (logout request))
  (GET "/register" [] (get-registration-page))
  (POST "/register" request (handle-registration request))
)
