(ns recepti.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            ;[noir.util.middleware :as noir-middleware]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [recepti.routes.auth :refer [auth-routes]]
            [recepti.routes.home :refer [home-routes]]
            [recepti.routes.recepies :refer [recepti-routes]]
            [recepti.routes.utisci :refer [utisci-routes]]
            [recepti.routes.administracija :refer [administracija-routes]]
            ))

(defn init []
  (println "recepti se pokrece"))

(defn destroy []
  (println "recepti se gase"))

(def backend (session-backend))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes auth-routes recepti-routes utisci-routes administracija-routes home-routes app-routes)
      (handler/site)
      (wrap-authorization backend)
      (wrap-authentication backend)
      (wrap-base-url)
      )
  )



