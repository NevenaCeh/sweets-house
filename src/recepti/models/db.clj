(ns recepti.models.db
(:require [clojure.java.jdbc :as sql])
(:import java.sql.DriverManager))


(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//localhost:3306/recepti"
         :user "admin"
         :password "admin"})

(defn read-guests []
(sql/with-connection
db
(sql/with-query-results res
["SELECT * FROM guestbook ORDER BY timestamp DESC"]
(doall res))))


(defn save-message [name message]
(sql/with-connection
db
(sql/insert-values
:guestbook
[:name :message :timestamp]
[name message (new java.util.Date)])))

