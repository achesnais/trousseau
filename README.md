# Trousseau

`[achesnais/trousseau "0.1.2"]`

Trousseau is a minimalist kitchensink library for Clojure.

- `trousseau.core` exposes a small set of useful functions like `map-vals` or `proportions`. This namespace also exposes an `inject` function which allows you to seemlessly include its contents into your `util` namespace.
- `trousseau.time` provides an API for dealing with time in Clojure.

## Usage

### `trousseau.core`
Very often when I spin up a new Clojure environment, either for playing around or as a new project, I found myself needing a set of very basic functions like `map-vals`, `map-keys`, etc. For this I'd use a kitchensink library like `plumbing.core`. However, after a while I'd also end up creating a `util` namespace in my project. Now, in most namespaces I'd end up having to require *2* namespaces to have access to useful functions.

The idea of `trousseau.core` is that you should be able to use it with `refer :all` in your `ns` declaration, and when the time comes when it's no longer enough, easily make its functions available in your project's `util` namespace.

`trousseau.core` was also written with the explicit aim to be fully referred in namespaces, in the context of projects where functions like `map-vals` and `map-keys` are accepted as idiomatic.

#### As a normal library:

```clojure
(ns sandbox.core
  (require [trousseau.core :refer :all]))

(map-vals inc {:a 1 :b 2}) ;;=> {:a 2 :b 3}
```

#### Injecting `trousseau.core`

Use `trousseau.core/inject` to put all `trousseau.core` functions in your namespace.

```clojure
(ns sandbox.util
  (require [trousseau.core :as trousseau]))

;; You'll probably want to leave a comment explaining what this does, if you're working on a collaborative project.
(trousseau/inject)
```

After this you'll be able to do the following:

```clojure
(ns sandbox.core
  (require [sandbox.util :refer :all]))

(map-vals inc {:a 1 :b 2}) ;;=> {:a 2 :b 3}
```

Tada! No more need to call two different kitchensink namespaces!

### `trousseau.time`
`trousseau.time` is based on `java.util.time`, which means you'll need Java 8 to use it. It aims to be a somewhat lightweight wrapper to make it straightforward to create date objects, manipulate them (comparison, adding time periods) as well as convert to and from `java.util.Date` (created in Clojure with the `#inst` reader litteral) and `java.sql.Timestamp`

All you need to know is:
- `trousseau.time/date-time` is your go to function. I still need to implement an easy way to create it from strings – stay tuned!
- If you don't care about time zones, use `trousseau.time/utc-date-time`
- Since the object returned by `trousseau.time/date-time` is a java.time.ZonedDateTime object, you get to use the full `java.time` API with it.

```clojure
(ns sandbox.time-things
  (require [trousseau.time :as time]))

(def date-a (time/utc-date-time #inst "2017"))
(def date-b (time/utc-date-time #inst "2016"))

(time/before? date-a date-b) ;;=> false
(time/minus date-a (time/months 2)) ;;=> #object[java.time.ZonedDateTime 0x147615ea "2016-11-01T00:00Z[UTC]"]

(time/periodic-seq date-b date-a (time/days 1)) ;;=> all the days in 2016
```

## How stable is this?

This project is currently under active construction, so it should be considered alpha. Until the 1.0 release I won't follow Semantic Versioning, so expect breaking changes!

## License

Copyright © 2017 Antoine Chesnais

Distributed under the Eclipse Public License either version 1.0.