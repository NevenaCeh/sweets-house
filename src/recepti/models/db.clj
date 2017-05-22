(ns recepti.models.db
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as str]
            [korma.core :refer :all]
            [korma.db :refer :all]
            )
  (:import java.sql.DriverManager))


(defdb db (mysql {:db "recepti"
                     :user "admin"
                     :password "admin"}))

(def dbs {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//localhost:3306/recepti"
         :user "admin"
         :password "admin"})

(defentity user
  (table :korisnici))

(defentity admin
  (table :admini))

(defentity recept
  (table :recepti))

(defentity utisak
  (table :utisak))

(defn vrati-korisnika [username password]
  (select user
  (where {:username username :password password})))

(defn vrati-admina [username password]
  (select admin
  (where {:username username :password password})))

(defn dodaj-korisnika [ime prezime email username password]
  (insert user
  (values {:ime ime
           :prezime prezime
           :email email
           :username username
           :password password
            })))

(defn vrati-recepte []
  (select recept (where {:dozvoljeno true}) (order :napisano :DESC)))

(defn vrati-utiske []
  (select utisak
  (order :datum :DESC)))

(defn vrati-poslednji-utisak []
  (select count utisak))

(defn dodaj-recept [naziv sastojci opis slika napisano receptod dozvoljeno]
  (insert recept
  (values {:naziv naziv
           :sastojci sastojci
           :opis opis
           :slika slika
           :napisano napisano
           :receptod receptod
           :dozvoljeno dozvoljeno
            })))

(defn dodaj-utisak [naziv opis ostavio datum]
  (insert utisak
  (values {:naziv naziv
           :opis opis
           :ostavio ostavio
           :datum datum
            })))

(defn obrisi-utisak [id]
  (delete utisak
  (where {:idUtiska id})))

(defn vrati-utisak [id]
  (select utisak
  (where {:idUtiska id})))
