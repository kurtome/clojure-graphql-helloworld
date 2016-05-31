(defproject http-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [compojure "1.5.0"]
                 [http-kit "2.1.19"]
                 [javax.servlet/servlet-api "2.5"]
                 [com.fzakaria/slf4j-timbre "0.3.2"]
                 [org.slf4j/slf4j-api "1.7.14"]
                 [com.graphql-java/graphql-java "2.0.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [ring-logger-timbre "0.7.5"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-devel "1.4.0"]]
  :main ^:skip-aot http-sample.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
