(defproject recepti "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies  [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [korma/korma "0.4.3"]
                 [selmer "1.10.7"]
                 [buddy/buddy-auth "1.4.1"]
                 [ring-server "0.4.0"]
                 [lib-noir "0.7.6"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                            javax.jms/jms
                            com.sun.jdmk/jmxtools
                            com.sun.jmx/jmxri]]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler recepti.handler/app
         :init recepti.handler/init
         :destroy recepti.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})


