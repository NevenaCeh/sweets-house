(ns recepti.routes.auth
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [clojure.string :refer [blank?]]
            [recepti.models.db :as db]
            [ring.util.response :refer [redirect]]
  )
)

(defn get-login-page [&[error]]
  (render-file "templates/login.html" {:error error}))

(defn handle-login [{:keys [params session] request :request}]
  (let [user (first (db/vrati-korisnika (:username params) (:password params)))]
    (println (some? user))
    (if (some? user)
      (assoc (redirect "/"):session (assoc session :identity user))
      (render-file "templates/login.html" {:error "Pogresno uneti kredencijali!!!"}))))

(defn logout
  [request]
  (-> (redirect "/login")
      (assoc :session {})))

(defn get-registration-page [&[error]]
  (render-file "templates/register.html" {:error error}))

(defn handle-registration [{:keys [params session] request :request}]
  (let [ime (:ime (:params request))
        prezime (:prezime (:params request))
        email (:email (:params request))
        username (:username (:params request))
        password (:password (:params request))
        session (:session request)]
  (if (or (blank? ime) (blank? prezime) (blank? email) (blank? username) (blank? password))
    (render-file "templates/register.html" "Molimo Vas da popunite sva polja!")
  (do
    (try
      (db/dodaj-korisnika (assoc (:params request)))
      (assoc (redirect "/"):session (assoc session :identity username))
      (catch Exception  e (render-file "templates/register.html" {:error (str e "Vec postoji korisnik: " username) }))))))

  ;;(assoc (redirect "/"):session (assoc session :identity ""))
)


(defroutes auth-routes
  (GET "/login" [] (get-login-page))
  (POST "/login" request (handle-login request))
  (GET "/logout" request (logout request))
  (GET "/register" [] (get-registration-page))
  (POST "/register" request (handle-registration request))
)
