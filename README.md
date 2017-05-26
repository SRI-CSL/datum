# datum

A parser for the [Human Readable Datum Format](http://pl.csl.sri.com/datumkb.html) used by [Pathway Logic](http://pl.csl.sri.com/index.html). Takes in a file or directory of datums, does some basic sanity checking, and emits JSON.

## Usage

    $ java -jar datum-0.1.0-standalone.jar [args] [files/directories]

## Options

    Options:
      -e, --errors         Print errors
      -o, --ops-file FILE  Provide an external ops json file
      -j, --json           Print parsed datums as JSON
      -J, --pretty-json    Pretty-print parsed datums as JSON
      -D, --duplicates     Print a list of duplicate datums
      -m, --merge          Merge datums that only differ in extras.

## Ops

For sanity and type checking, you will need an `ops file`, which is a JSON file containing a Maude type hierarchy of relevant sorts and ops. The parser is bundled with some default ops. Instructions for generating your own TBD.

## License

Released under the MIT license. See LICENSE for the full license.
