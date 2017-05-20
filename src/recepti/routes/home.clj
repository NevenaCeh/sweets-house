(ns recepti.routes.home
  (:require [compojure.core :refer :all]
            [recepti.views.layout :as layout]
            [selmer.parser :refer [render-file]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :refer [response redirect content-type]]
  )
)

(defn home []
  (render-file "templates/homepage.html" {:title ""})
)

(defroutes home-routes
  (GET "/" [] (home)))
