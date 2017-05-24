(ns recepti.routes.auth
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [clojure.string :refer [blank?]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
            ;;[struct.core :as st]
  )
)

(defn get-login-page [&[error]]
  (render-file "templates/login.html" {:error error}))

(defn get-admin-login-page [&[error]]
  (render-file "templates/admin-login.html" {:error error}))

(defn handle-admin-login [{:keys [params session] request :request}]
  (println params)
  (let [admin (first (db/vrati-admina (:username params) (:password params)))]
    (println (some? admin))
    (if (some? admin)
      (assoc (redirect "/admin"):session (assoc session :identity admin)))
      (render-file "templates/admin-login.html" {:error "Pogresno uneti kredencijali za admina!!!"})))

(defn handle-user-login [{:keys [params session] request :request}]
  (let [user (first (db/vrati-korisnika (:username params) (:password params)))]
    (println (some? user))
    (if (some? user)
      (assoc (redirect "/"):session (assoc session :identity user)))
      (render-file "templates/login.html" {:error "Pogresno uneti kredencijali!!!"})))

(defn handle-login [{:keys [params session] request :request}]
  (cond
    (or (= (:username params) "admin") (= (:password params) "admin"))
    (do
         (println "Jeste admin user")
         (let [admin (first (db/vrati-admina (:username params) (:password params)))]
                (println (some? admin))
         (if (some? admin)
                (assoc (redirect "/admin"):session (assoc session :identity admin))
                (render-file "templates/login.html" {:error "Pogresno uneti kredencijali za admina!!!"})))
    )
    :else (do
          (println "Nije admin user")
          (let [user (first (db/vrati-korisnika (:username params) (:password params)))]
          (println (some? user))
          (if (some? user)
            (assoc (redirect "/"):session (assoc session :identity user))
            (render-file "templates/login.html" {:error "Pogresno uneti kredencijali!!!"})))
          )))

(defn logout
  [request]
  (-> (redirect "/login")
      (assoc :session {})))

(defn logout-admin
  [request]
  (-> (redirect "/login")
      (assoc :session {})))

(defn get-registration-page [&[error]]
  (render-file "templates/register.html" {:error error}))

(defn handle-registration [{:keys [params session] request :request}]
  (let [ime (:ime params)
        prezime (:prezime params)
        email (:email params)
        username (:username params)
        password (:password params)]
      (db/dodaj-korisnika ime prezime email username password)
      (let [user (first (db/vrati-korisnika (:username username) (:password password)))]
        (assoc (redirect "/"):session (assoc session :identity user))
      )

  )
)

(defroutes auth-routes
  (GET "/login" [] (get-login-page))
  ;(GET "/adminlogin" [] (get-admin-login-page))
  (POST "/login" request (handle-login request))
  ;(POST "/adminlogin" request (handle-admin-login request))
  (GET "/logout" request (logout request))
  ;(GET "/adminlogout" request (logout-admin request))
  (GET "/register" [] (get-registration-page))
  (POST "/register" request (handle-registration request))
)
