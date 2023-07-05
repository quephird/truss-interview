# truss-interview

This is my submission to one of the problems for the Truss interview process.

## Usage

You will need two things to run this project: Clojure and Leinengen. 
Clojure is the langauge in which this project is written, and Leinengen is a tool for, among other things, dependency management and building and running Clojure projects.

You can install Clojure via Homebrew

```
brew install clojure
```

... as well as Leiningen

```
brew install leiningen
```

Once you have both installed, you can run this project by issuing the following:

```
lein run <<input CSV file name>> <<output CSV file name>>
```

... and that should process the input file and produce the desired output file.