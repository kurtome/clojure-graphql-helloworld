(ns http-sample.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [clojure.tools.cli :refer [parse-opts]]
            [compojure.route :as route :only [files not-found]]
            [ring.logger.timbre :as ring-logger]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]])
  (:import [graphql GraphQL]
           [graphql.schema GraphQLObjectType GraphQLSchema])
  )

(defn newObject [] (graphql.schema.GraphQLObjectType/newObject))
(defn newFieldDefinition [] (graphql.schema.GraphQLFieldDefinition/newFieldDefinition))
(def GraphQLString graphql.Scalars/GraphQLString)

(def queryType
  (-> (newObject) 
      (.name "helloWorldQuery")
      (.field (-> (newFieldDefinition)
                  (.type GraphQLString)
                  (.name "hello")
                  (.staticValue "world")
                  (.build)))
      (.build))
  )

(def schema (-> (GraphQLSchema/newSchema)
                (.query queryType)
                (.build)))

(def graphql (GraphQL. schema))

(def cli-options
    ;; An option with a required argument
    [["-p" "--port PORT" "Port number"
          :default 8080
          :parse-fn #(Integer/parseInt %)
          :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
     ;; A boolean option defaulting to nil
     ["-h" "--help"]]
  )


(defroutes all-routes
    (GET "/" [] "handling-page")
    (GET "/graphql" [] (str (-> graphql
                                (.execute "{hello}")
                                (.getData))))
    (route/not-found "<p>Page not found.</p>")) ;; all other, return 404


(defn run-sample-server [opts]
  (let [handler (site all-routes)
        port (get opts :port)
        ]
    (do
      (run-server (ring-logger/wrap-with-logger handler) {:port port})
      (println "Started server.")
      (println (str "Listening on HTTP " port ".")))
  ))

(defn -main [& args]
    (let [opts-info (parse-opts args cli-options)
          opts-errors (get opts-info :errors)]
      (do
        (if (or opts-errors (-> opts-info :options :help))
          (do (if opts-errors (doseq [err opts-errors] (println err))) (println (get opts-info :summary)))
          (run-sample-server (-> opts-info :options))))))
