(ns recepti.routes.home
  (:require [compojure.core :refer :all]
            [recepti.views.layout :as layout]
            [selmer.parser :refer [render-file]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :refer [response redirect content-type]]
  )
)

(defn authenticated [session]
  (authenticated? session))

(defn home [session]
  (cond
    (not (authenticated session))
     (redirect "/login")
    :else
     (render-file "templates/homepage.html"
                 {:title "Home"
                  :user (:identity session)
                 })
  )
)


(defroutes home-routes
  (GET "/" request (home (:session request))))
