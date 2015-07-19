# Tundra ❄

A package of cool services for [webMethods Integration Server] 7.1 and
higher.

## Related

See also [TundraTN], a package of cool services for [webMethods Trading
Networks] 7.1 and higher.

## Dependencies

[Tundra] is compiled for Java 1.6, and is dependent on the following
[webMethods Integration Server] packages:

* `WmFlatFile`
* `WmPublic`
* `WmRoot`

[Tundra] is also dependent on the [Tundra.java] project, which provides the
underlying implementation for the majority of services, and is included in
the [Tundra] package as a [JAR] library in the following location:

* `./code/jars/static/Tundra.jar`

## Installation

You have two choices for installing [Tundra]:

* `git`
* `zip`

If you are comfortable using git, I recommend this method as you can then
easily switch between package versions using git checkout and download new
versions using git fetch.

### Using Git

To install with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your Integration
  Server.
* Your Integration Server has internet access to https://github.com (for
  cloning the repository).
* The dependent packages listed above are installed and enabled on your
  Integration Server.
* You have identified what version of [Tundra] you'd like to install by
  referring to the available [releases].

From your Integration Server installation:

```sh
$ cd ./packages/
$ git clone https://github.com/Permafrost/Tundra.git
$ cd ./Tundra/
$ git checkout v<n.n.n> # where <n.n.n> is the required version
```

Then restart Integration Server to complete the installation.

### Using Zip

1. Download a pre-built zip of the desired version of the package from the
   available [releases].
2. Copy the `Tundra-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the [Tundra] package release `Tundra-vn.n.n.zip` from
   the package management web page on your Integration Server's web
   administration site.
4. Restart Integration Server to complete the installation.

## Upgrading

When upgrading you have to choose the same method used to originally install
the package. Unfortunately, if git wasn't used to install the package then
you can't use git to upgrade it either. However, if you want to switch to
using git to manage the package, delete the installed package and start over
using the git method for installation.

### Using Git

To upgrade with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your Integration
  Server.
* Your Integration Server has internet access to https://github.com (for
  fetching updates from the repository).
* The dependent packages listed above are installed and enabled on your
  Integration Server.
* You have identified what version of [Tundra] you'd like to upgrade to by
  referring to the available [releases].
* You originally installed [Tundra] using the git method described above.

From your Integration Server installation:

```sh
$ cd ./packages/Tundra/
$ git fetch
$ git checkout v<n.n.n> # where <n.n.n> is the desired updated version
```

Then restart Integration Server to complete the upgrade.

### Using Zip

1. Download a pre-built zip of the desired updated version of the package
   from the available [releases].
2. Copy the `Tundra-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the updated [Tundra] package release
   `Tundra-vn.n.n.zip` from the package management web page on your
   Integration Server's web administration site.
4. Restart Integration Server to complete the installation.

## Conventions

1. All input and output pipeline arguments are prefixed with `$` as a poor-
   man's scoping mechanism, since typically user-space variables are
   unprefixed.
2. All boolean arguments are suffixed with a `?`.
3. Single-word argument names are preferred. Where multiple words are
   necessary, words are separated with a `.`.
4. Service namespace is kept flat. Namespace folders are usually nouns.
   Service names are usually verbs, indicating the action performed on the
   noun (parent folder).
5. Services declare all inputs and outputs, always explicitly marked as
   optional or required, use constrained types where possible, and enable
   input and output pipeline validation, which minimises developer surprise.
6. Services often declare their inputs as optional, and either apply an
   appropriate default value, or take no action and return no output
   (whatever is more appropriate), to minimise the need for existence
   checking in flow map steps.
7. All private elements are kept in the `tundra.support` folder. All other
   elements comprise the public API of the package. As the private elements
   do not contribute to the public API, they are liable to change at any
   time. **Enter at your own risk.**

## Tests

Most services in Tundra have unit tests, located in the `tundra.support.test`
folder. To run the unit tests either:

* Run the `Tundra/tundra:test($package = "Tundra")` service directly, or
* Visit the <http://server:port/invoke/tundra/test?$package=Tundra> web page.

## Services

### tundra:log

Writes a message to the server log, automatically prefixed with the call
stack.

#### Inputs:

* `$message` is the message to be written to the server log.
* `$level` is the logging level of the message:
  * `Fatal`
  * `Error`
  * `Warn`
  * `Info`
  * `Debug`
  * `Trace`
  * `Off`

---

### tundra:test

Runs all `*test*:should*` services in the given package, returning the test
results, where a test case passes if no exceptions are thrown.

Refer to the `Tundra/tundra.support.test` folder for test case service
examples.

This service is designed to either be invoked directly, or via a web
browser. When invoked via a browser, a basic HTML test report is displayed:

    http://server:port/invoke/tundra/test?$package=Tundra

#### Inputs:

* `$package` is the name of the package which contains the test cases to
  be executed.


#### Outputs:

* `$result` is a document containing the test results from executing all
  the `*test*:should*` services in the given `$package`.
  * `package` is the name of package the test results relate to.
  * `passed?` is a boolean indicating if all test cases executed passed.
  * `counts` is a document containing the test result counts.
    * `total` is the total number of test case services executed.
    * `passed` is the number of test case services which passed (did not
      throw an exception).
    * `failed` is the number of test case services which failed (threw an
      exception).
  * `cases` is a list of the test case services which were executed.
    * `description` is the fully-qualified name of the test case service.
    * `passed?` is a boolean indicating if the test case service passed
      (did not throw an exception).
    * `message` is an optional description returned if the test case
      service failed, which describes why the test case failed.

---

### tundra.assertion.datetime:equal

Throws an assertion error if the expected and actual datetimes are not
equal.

#### Inputs:

* `$expected` is the expected datetime string value.
* `$actual` is the actual datetime string value. If this value is not
  equal to the expected value, an assertion error will be thrown.
* `$pattern` is an optional datetime string pattern the above values
  conform to, which defaults to an ISO8601/XML datetime pattern. Can
  either be a [java.text.SimpleDateFormat] pattern, or one of the
  following handful of well-known named patterns:
  * `datetime`       - ISO8601/XML datetime
  * `datetime.jdbc`  - yyyy-MM-dd HH:mm:ss.SSS
  * `date`           - ISO8601/XML date
  * `date.jdbc`      - yyyy-MM-dd
  * `time`           - ISO8601/XML time
  * `time.jdbc`      - HH:mm:ss
  * `milliseconds`   - The number of milliseconds since the Epoch,
                       January 1, 1970 00:00:00.000 GMT (Gregorian)
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.datetime:unequal

Throws an assertion error if the expected and actual datetimes are equal.

#### Inputs:

* `$expected` is the expected datetime string value.
* `$actual` is the actual datetime string value. If this value is equal
  to the expected value, an assertion error will be thrown.
* `$pattern` is an optional datetime string pattern the above values
  conform to, which defaults to an ISO8601/XML datetime pattern. Can
  either be a [java.text.SimpleDateFormat] pattern, or one of the
  following handful of well-known named patterns:
  * `datetime`       - ISO8601/XML datetime
  * `datetime.jdbc`  - yyyy-MM-dd HH:mm:ss.SSS
  * `date`           - ISO8601/XML date
  * `date.jdbc`      - yyyy-MM-dd
  * `time`           - ISO8601/XML time
  * `time.jdbc`      - HH:mm:ss
  * `milliseconds`   - The number of milliseconds since the Epoch,
                       January 1, 1970 00:00:00.000 GMT (Gregorian)
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.document:equal

Throws an assertion error if the expected and actual documents are not
equal.

#### Inputs:

* `$expected` is the expected IData document.
* `$actual` is the actual IData document. If this document is not equal
  to the expected document, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.document:unequal

Throws an assertion error if the expected and actual documents are equal.

#### Inputs:

* `$expected` is the expected IData document.
* `$actual` is the actual IData document. If this document is equal to
  the expected document, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.exception:raised

Throws an assertion error if the given service, when invoked, does
not throw an exception matching the given criteria.

#### Inputs:

* `$service` is the fully-qualified name of the service to be
  invoked that is expected to throw an exception.
* `$pipeline` is an optional IData document which, if specified,
  contains the input arguments for the invocation of `$service`; in
  other words, the invocation is scoped to this IData document. If
  not specified, the invocation is unscoped, and hence the service
  will operate directly against the pipeline itself.
* `$exception` is an IData document containing optional criteria
  which a thrown exception will be expected meet.
  * `class` is the name of a Java class that the thrown exception
    must be an instance of.
  * `message`
    * `pattern` is a [regular expression pattern] or literal string
      that must match the thrown exception's message.
    * `literal?` is a boolean indicating if the `pattern` string
      should be treated as a literal string. If false, `pattern` is
      treated as a [regular expression pattern]. If true, `pattern`
      is treated as a literal string. Defaults to false, if not
      specified.
* `$message` is an optional custom message to be used as the
  asertion error message if the assertion fails.

---

### tundra.assertion.list.document:equal

Throws an assertion error if the expected and actual document lists are not
equal.

#### Inputs:

* `$expected` is the expected IData document list.
* `$actual` is the actual IData document list. If this document list is
  not equal to the expected document list, an assertion error will be
  thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.document:unequal

Throws an assertion error if the expected and actual document lists are
equal.

#### Inputs:

* `$expected` is the expected IData document list.
* `$actual` is the actual IData document list. If this document list is
  equal to the expected document list, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.object:equal

Throws an assertion error if the expected and actual lists are not equal.

#### Inputs:

* `$expected` is the expected java.lang.Object list.
* `$actual` is the actual java.lang.Object list. If this list is not
  equal to the expected list, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.object:exists

Throws an assertion error if the given list is null.

#### Inputs:

* `$list` is the java.lang.Object list to assert existence of.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.object:instance

Throws an assertion error if the given list is not an instance of the given
class.

#### Inputs:

* `$list` is the java.lang.Object list to be tested as an instance of the
  specified class.
* `$class` is the Java array class name the given list is asserted to be
  an instance of. Note that Java array class names are [different and
  distinct][jacn] to normal Java object class names. For example, an
  array of java.lang.String objects has the class name
  `[Ljava.lang.String;`.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

[jacn]: <http://docs.oracle.com/javase/tutorial/reflect/special/arrayComponents.html>

---

### tundra.assertion.list.object:nothing

Throws an assertion error if the given list is not null.

#### Inputs:

* `$list` is the java.lang.Object list expected to be null.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.object:unequal

Throws an assertion error if the expected and actual lists are equal.

#### Inputs:

* `$expected` is the expected object list.
* `$actual` is the actual object list. If this list is equal to the
  expected list, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.string:equal

Throws an assertion error if the expected and actual string lists are not
equal.

#### Inputs:

* `$expected` is the expected string list.
* `$actual` is the actual string list. If this list is not equal to the
  expected list, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.list.string:unequal

Throws an assertion error if the expected and actual string lists are
equal.

#### Inputs:

* `$expected` is the expected string list.
* `$actual` is the actual string list. If this list is equal to the
  expected list, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.object:equal

Throws an assertion error if the expected and actual objects are not equal.

#### Inputs:

* `$expected` is the expected object value.
* `$actual` is the actual object value. If this value is not equal to the
  expected value, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.object:exists

Throws an assertion error if the given object is null.

#### Inputs:

* `$object` is the object expected to not be null.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.object:instance

Throws an assertion error if the given object is not an instance of the
given class.

#### Inputs:

* `$object` is the object to be tested as an instance of the specified
  class.
* `$class` is the Java class name the given object is asserted to be an
  instance of.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.object:nothing

Throws an assertion error if the given object is not null

#### Inputs:

* `$object` is the object that is expected to be null.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.object:unequal

Throws an assertion error if the expected and actual objects are equal.

#### Inputs:

* `$expected` is the expected object.
* `$actual` is the actual object. If this object is equal to the expected
  object, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.step:unreached

Throws an assertion error if this service is executed.

#### Inputs:

* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.string:equal

Throws an assertion error if the expected and actual strings are not equal.

#### Inputs:

* `$expected` is the expected string value.
* `$actual` is the actual string value. If this value is not equal to the
  expected value, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.assertion.string:unequal

Throws an assertion error if the expected and actual strings are equal.

#### Inputs:

* `$expected` is the expected string value.
* `$actual` is the actual string value. If this value is equal to the
  expected value, an assertion error will be thrown.
* `$message` is an optional custom message to be used as the assertion
  error message if the assertion fails.

---

### tundra.base64:decode

[Base64] decodes the given string, byte array, or input stream.

#### Inputs:

* `$base64` is either a [Base64] encoded string, byte array, or input
  stream to be decoded.
* `$encoding` is the optional character set used to decode the text data
  when `$base64` is provided as a byte array or input stream. Defaults to
  [UTF-8]. Not used when `$base64` is provided as a string.
* `$mode` is an optional choice of stream, bytes, or string which
  determines the type of the output `$content` object. Defaults to
  stream.

#### Outputs:

* `$content` is the [Base64] decoded data as a string, byte array, or
  input stream (depending on the `$mode` chosen).

---

### tundra.base64:encode

[Base64] encodes the given string, byte array, or input stream.

#### Inputs:

* `$content` is either a string, byte array, or input stream containing
  data to be [Base64] encoded.
* `$encoding` is the optional character set used to decode the text data
  when `$content` is provided as a byte array or input stream. Defaults
  to [UTF-8]. Not used when `$base64` is provided as a string.
* `$mode` is an optional choice of stream, bytes, or string which
  determines the type of the output `$base64` object. Defaults to stream.

#### Outputs:

* `$base64` is the [Base64] encoded data as a string, byte array, or input
  stream (depending on the `$mode` chosen).

---

### tundra.bool:emit

Converts the given `$boolean` value to a string using the appropriate
string values specified for true and false.

#### Inputs:

* `$boolean` is the value to be converted, and can be either a string
  parseable as a boolean value, or a `java.lang.Boolean` object.
* `$value.true` is an optional string value returned if `$boolean` is
  true. Defaults to "true".
* `$value.false` is an optional string value returned if `$boolean` is
  false. Defaults to "false".

#### Outputs:

* `$string` is the converted boolean value.

---

### tundra.bool:format

Formats the given boolean string using the appropriate
boolean value specified for true and false.

#### Inputs:

* `$string` is the string value to be formatted.
* `$value.true.input` is an optional string value used to determine
  if `$string` represents a boolean value of `true`.
* `$value.false.input` is an optional string value used to determine
  if `$string` represents a boolean value of `false`.
* `$value.true.output` is an optional string value returned if
  `$string` represents the boolean value `true`. Defaults to "true".
* `$value.false.output` is an optional string value returned if
  `$string` represents the boolean value `false`. Defaults to "false".

#### Outputs:

* `$string` is the reformatted boolean string.

---

### tundra.bool:negate

Returns the negated canonical string form for the given `$boolean` string:
either "true" or "false".

#### Inputs:

* `$boolean` is the value to be negated.

#### Outputs:

* `$boolean` is the negated input value.

---

### tundra.bool:normalize

Returns the canonical string form for the given $boolean string: either
"true" or "false".

If `$boolean` is null and `$default` is not null, then `$default`'s boolean
value will be returned.

If `$boolean` is null and `$default` is null, then "false" will be
returned.

#### Inputs:

* `$boolean` is the value to be normalized.
* `$default` is the value to use if `$boolean` is null.

#### Outputs:

* `$boolean` is the normalized input value, guaranteed to be either the
  string "true" or "false".

---

### tundra.bool:parse

Parses the given string as a boolean value.

#### Inputs:

* `$string` is an optional string to be parsed.

#### Outputs:

* `$boolean` is the resulting `java.lang.Boolean` object from parsing
  the given `$string`.

---

### tundra.bytes:length

Returns the length of the given byte array.

#### Inputs:

* `$bytes` must be a byte array.

#### Outputs:

* `$length` is the length of the given byte array.

---

### tundra.bytes:normalize

Converts a string, byte array or input stream to a byte array.

#### Inputs:

* `$object` is the string, byte array or input stream to be normalized.
* `$encoding` is the optional character set to use to encode `$object`
  when it is a string. Defaults to [UTF-8].

#### Outputs:

* `$bytes` is a byte array representation of the input `$object` data.

---

### tundra.condition:evaluate

Evaluates the given condition against the pipeline (or optional scope IData
document).

#### Inputs:

* `$condition` is the conditional statement to be evaluated. Conditional
  statements have the same form as when they are used in a flow branch
  step:

      condition = value comparison_op value [logical_op condition]
                | [!]value                  [logical_op condition]

  Where:
  * `value` is a fully-qualified percent delimited IData document key,
    such as `%a/b/c[0]%`, or a literal (double- or single-quoted) string,
    number, (forward slash delimited) regular expression, or `$null`.
  * `comparison_op` is one of the following comparison operators:
    * `=`
    * `==`
    * `!=`
    * `<>`
    * `>`
    * `>=`
    * `<`
    * `<=`
  * `logical_op` is one of the following logical operators:
    * `and`
    * `&&`
    * `or`
    * `||`

  Examples:
  * `%a/b/c[0]% == "xyz"`
  * `%num% == /\d\d/`
  * `%num% == 10`
  * `%something% == $null`
  * `%total% == %count%`
  * `%inString1% == "abc" and (%inNum1% < 100 or %inNum2% > 1000)`

  Refer to the Conditional Expressions section in the Integration Server
  Developer User's Guide for further details.
* `$scope` is an optional IData document containing the variables against
  which `$condition` will be evaluated. If not specified, the
  `$condition` will be evaluated against the pipeline.

#### Outputs:

* `$result?` is the boolean result of the evaluation. If no `$condition`
  was specified, true will be returned.

---

### tundra.content:amend

Edits the given content with the list of key value pairs specified in
`$amendments` by first parsing the content, replacing the values
associated with the given keys with those in `$amendments`, and then
emitting or serializing the amended content.

#### Inputs:

* `$content` is a string, byte array, or input stream containing the
  content to be amended.
* `$amendments` is an IData document list containing all the edits to be
  made to the given `$content`.
  * `key` is a fully-qualified (for example, `a/b/c[0]`) key identifying
    the value in the parsed `$content` to be edited.
  * `value` is the value to be assigned to the item identified by key,
    and can include percent-delimited variable substitution strings
    which will be substituted prior to being inserted into the parsed
    `$content`.
  * `condition` is an optional `Tundra/tundra.condition:evaluate`
    conditional statement, which is evaluated against the pipeline and
    only if the condition evaluates to true will the associated amended
    value be applied. If not specified, the amended value will always be
    applied.
* `$content.type` is the MIME media type that describes the format of the
  given `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when parsing and emitting [XML] content with elements in one or
  more namespaces.
* `$schema` is the fully-qualified name of the parsing schema to use when
  parsing `$content` as [XML] or Flat File content, and can have the
  following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to parsing `$content` as [XML], if neither `$content.type` nor
  `$schema` are specified.
* `$encoding.input` is an optional character set used to decode the text
  data if `$content` is provided as a byte array or input stream.
  Defaults to [UTF-8].
* `$encoding.output` is an optional character set used to encode the
  amended text data if `$mode.output` is a byte array or input stream.
  Defaults to [UTF-8].
* `$mode.output` is an optional choice of stream, bytes, or string which
  specifies the type of object `$content` is returned as. Defaults to
  stream.

#### Outputs:

* `$content` is the resulting edited content.

---

### tundra.content:deliver

Delivers arbitrary content (string, bytes, or input stream) to the given
destination URI.

Additional delivery protocols can be implemented by creating a service
named for the URI scheme in the folder `Tundra/tundra.content.deliver`.
Services in this folder should implement the
`Tundra/tundra.schema.content.deliver:handler` specification.

#### Inputs:

* `$content` is a string, byte array, or input stream containing data to
  be delivered to the `$destination` URI.

  If `$content` is provided as an IData document, it will be serialized
  using an emitter determined in order of precedence by `$schema` and
  `$content.type`. If `$schema` is specified, the type of reference
  determines the emitter to use: a document reference will use the XML
  emitter, a flat file schema reference will use the Flat File emitter.
  If `$schema` is not specified, `$content.type` is used to determine
  the most appropriate emitter for the MIME media type in question. If
  neither `$schema`, nor `$content.type` are specified, `$content` is
  serialized as XML by default.

  Emitter implementions are as follows:
  * [CSV]: `Tundra/tundra.csv:emit`
  * Flat File: `WmFlatFile/pub.flatFile:convertToString`
  * [JSON]: `Tundra/tundra.json:emit`
  * Pipe separated values: `Tundra/tundra.csv:emit`
  * [TSV]: `Tundra/tundra.csv:emit`
  * [XML]: `WmPublic/pub.xml:documentToXMLString`
  * [YAML]: `Tundra/tundra.yaml:emit`
* $content.type is the MIME media type that describes the format of the
  given content:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when emitting and IData document as [XML] content with elements
  in one or more namespaces.
* `$schema` is the fully-qualified name of the parsing schema to use to
  serialize `$content` (when provided as an IData document) to [XML] or
  Flat File content, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Defaults to serializing `$content` as [XML], if neither `$content.type`
  nor `$schema` are specified.
* `$encoding` is an optional character set to use when `$content` is
  provided as a string or IData document used to encode the text data
  upon delivery. Defaults to [UTF-8].
* `$destination` is a URI identifying the location where the given
  `$content` should be delivered. If not specified, no delivery will be
  attempted. Supports the following delivery protocols / URI schemes:
  * `file`: writes the given content to the file specified by the
    destination URI. The following additional options can be provided via
    the `$pipeline` document:
    * `$mode`: append / write
  * `ftp`: uploads the given content to the FTP server, directory and
    file specified by the destination URI. An example FTP URI is as
    follows:

        ftp://aladdin:opensesame@example.com:21/path/file?append=true&active=true&ascii=true

    The following additional options can be provided via the `$pipeline`
    document:
    * `$user` is the username used to log in to the FTP server. Defaults
      to the username specified in the authority section of the URI, if
      not specified.
    * `$password` is the password used to log in to the FTP server.
      Defaults to the password specified in the authority section of the
      URI, if not specified.
    * `$active` is a boolean which when true indicates that the
      connection to the FTP server should be in active mode. Defaults to
      false (passive mode), if not specified.
    * `$append` is a boolean which when true will append the given
      content to the file, rather than overwrite it, if the file already
      exists. Defaults to false (overwriting), if not specified.
    * `$ascii` is a boolean which when true indicates that the file
      transfer should be made in ascii mode. Defaults to false (binary
      mode), if not specified.
    * `$timeout` is an optional XML duration string which specifies how
      long the client waits for a response from the server before timing
      out and terminating the request with an error. Defaults to PT60S,
      if not specified.
  * `ftps`: refer to `ftp`
  * `http`: transmits the given content to the destination URI. The
    following additional options can be provided via the `$pipeline`
    document:
    * `$method`: get / put / post / delete / head / trace / options
    * `$headers/*`: additional HTTP headers as required
    * `$authority/user`: the username to log on to the remote web server
    * `$authority/password`: the password to log on to the remote web
      server
    * `$timeout` is an optional XML duration string which specifies how
      long the client waits for a response from the server before timing
      out and terminating the request with an error. Defaults to PT60S,
      if not specified.
  * `https`: refer to `http`
  * `jms`: sends the given content as a [JMS] [javax.jms.BytesMessage] to
    the specified [JMS] alias and queue or topic. The following
    additional settings can be specified:
    * $headers/*: additional properties to be added to the [JMS] message
      header, which can be used for filtering by [JMS] subscribers.

    The following example will deliver the given content as a [JMS] bytes
    message to the JMS alias DEFAULT_IS_JMS_CONNECTION, [JMS] topic
    JMS::Temporary::Topic, with a time to live of 1 day, and with the
    default priority of 4:

        jms://DEFAULT_IS_JMS_CONNECTION?topic=JMS::Temporary::Topic&lifetime=P1D

    The following example will deliver the given content as a [JMS] bytes
    message to the [JMS] alias DEFAULT_IS_JMS_CONNECTION, [JMS] queue
    JMS::Temporary::Queue, with no expiry, and with the specified
    priority of 1:

        jms://DEFAULT_IS_JMS_CONNECTION?queue=JMS::Temporary::Queue&priority=1
  * `mailto`: sends an email with the given content attached. An example
    mailto URI is as follows:

        mailto:bob@example.com?cc=jane@example.com&subject=Example&body=Example&attachment=message.xml

    The following additional override options can be provided via the
    `$pipeline` document:
    * `$attachment`: the attached file's name
    * `$from`: email address to send the email from
    * `$subject`: the subject line text
    * `$body`: the main text of the email
    * `$smtp`: an SMTP URI specifying the SMTP server to use (for
      example, `smtp://user:password@host:port`), defaults to the SMTP
      server configured in the Integration Server setting
      `watt.server.smtpServer`.
  * `sap+idoc`: sends an IDoc XML message to an SAP system. Both opaque
    and non-opaque URIs are allowed: opaque URIs are useful if the SAP
    Adapter alias contains characters not permitted in a normal domain
    name, such as underscores.

    An example opaque sap+idoc URI is as follows, where sap_r3 is the
    SAP Adapter alias name, and the user and password are provided as
    query string parameters:

        sap+idoc:sap_r3?user=aladdin&password=opensesame&client=200&language=en&queue=xyz

    An example non-opaque sap+idoc URI is as follows, where sappr3 is the
    SAP Adapter alias name, and the user and password are provided in the
    authority section of the URI:

        sap+idoc://aladdin:opensesame@sappr3?client=200&language=en&queue=xyz

    The following additional override options can be provided via the
    `$pipeline` document, and if specified will overrided the relevant
    parts of the destination URI:
    * `$user` is the username used for the SAP session. Defaults to the
      SAP Adapter alias username, if not specified.
    * `$password` is the password used for the SAP session. Defaults to
      the SAP Adapter alias password, if not specified.
    * `$client` is the SAP client used for the SAP session. Defaults to
      the SAP Adapter alias client, if not specified.
    * `$language` is the language used for the SAP session. Defaults to
      the SAP Adapter alias language, if not specified.
    * `$queue` is the optional name of the SAP system inbound queue,
      required when using queued remote function calls (qRFC).
* `$pipeline` is an optional IData document for providing arbitrary
  variables to the delivery implementation service.

#### Outputs:

* `$message` is an optional response message, useful for logging, that
  may be returned by specific delivery protocols.
* `$response` is an optional response content returned by the delivery
  (for example, the HTTP response body).
* `$response.type` is an optional MIME media type describing the type of
  `$response` returned.

---

### tundra.content.deliver:file

The file protocol handler for the `Tundra/tundra.content:deliver`
service, which delivers arbitrary content to a file URI
destination.

Implements the `Tundra/tundra.schema.content.deliver:handler`
specification.

---

### tundra.content.deliver:http

The HTTP protocol handler for the `Tundra/tundra.content:deliver`
service, which delivers arbitrary content to a HTTP URI
destination.

Implements the `Tundra/tundra.schema.content.deliver:handler`
specification.

---

### tundra.content.deliver:https

The HTTPS protocol handler for the `Tundra/tundra.content:deliver`
service, which delivers arbitrary content to a HTTPS URI
destination.

Implements the `Tundra/tundra.schema.content.deliver:handler`
specification.

---

### tundra.content.deliver:mailto

The email protocol handler for the `Tundra/tundra.content:deliver`
service, which delivers arbitrary content to a mailto URI
destination.

Implements the `Tundra/tundra.schema.content.deliver:handler`
specification.

---

### tundra.content:discard

Receives arbitrary (XML or flat file) content and then discards it (does
nothing with it). This is the Tundra equivalent of Unix's [/dev/null],
which is useful for successfully receiving messages that do not need to be
saved or processed.

This service is intended to be invoked by clients via HTTP or FTP.

[/dev/null]: <http://en.wikipedia.org/wiki//dev/null>

---

### tundra.content:emit

Emits (serializes) an IData document to a string, byte array, or input
stream.

Emitter implementions for the supported content types are as follows:
* [CSV]: Tundra/tundra.csv:emit
* Flat File: WmFlatFile/pub.flatFile:convertToString
* [JSON]: Tundra/tundra.json:emit
* Pipe separated values: Tundra/tundra.csv:emit
* [TSV]: Tundra/tundra.csv:emit
* [XML]: WmPublic/pub.xml:documentToXMLString
* [YAML]: Tundra/tundra.yaml:emit

#### Inputs:

* `$document` is the IData document to be serialized as a string, byte
  array, or input stream.
* `$content.type` is the MIME media type that describes the format of the
  resulting serialized `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when emitting [XML] content with elements in one or more
  namespaces.
* `$schema` is the fully-qualified name of the parsing schema to use when
  serializing `$document` to [XML] or Flat File content, and can have the
  following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Defaults to serializing `$document` as [XML], if neither
  `$content.type` nor `$schema` are specified.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to [UTF-8].
* `$mode` is an optional choice of {stream, bytes, string} which
  specifies the type of object `$content` is returned as. Defaults to
  stream.

#### Outputs:

* `$content` is the resulting serialization of `$document`.

---

### tundra.content:parse

Parses (deserializes) content specified as a string, byte array, or
input stream into an IData document.

Parser implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:parse`
* Flat File: `WmFlatFile/pub.flatFile:convertToValues`
* [JSON]: `Tundra/tundra.json:parse`
* Pipe separated values: `Tundra/tundra.csv:parse`
* [TSV]: `Tundra/tundra.csv:parse`
* [XML]: `WmPublic/pub.xml:xmlStringToXMLNode`, `pub.xml:xmlNodeToDocument`
* [YAML]: `Tundra/tundra.yaml:parse`

#### Inputs:

* `$content` is a string, byte array, or input stream containing content
  to be parsed.
* `$content.type` is the MIME media type that describes the format of the
  given `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when parsing [XML] content with elements in one or more
  namespaces.
* `$schema` is the fully-qualified name of the parsing schema to use when
  parsing `$content` as [XML] or Flat File content, and can have the
  following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to parsing `$content` as [XML], if neither `$content.type` nor
  `$schema` are specified.
* `$encoding` is an optional character set to use when `$content` is
  provided as a byte array or input stream to decode the contained text
  data. Defaults to [UTF-8].

#### Outputs:

* `$document` is the resulting IData document representing the parsed
  `$content`.

---

### tundra.content:reject

Receives arbitrary (XML or flat file) content and then rejects it by always
returning an error to the client.

This service is intended to be invoked by clients via HTTP or FTP.

---

### tundra.content:retrieve

Retrieves arbitrary content from the given `$source` URI, and calls the
given content processing service to process it.

Additional retrieval protocols can be implemented by creating a service
named for the URI scheme in the folder `tundra.content.retrieve`.
Services in this folder must implement the
`tundra.schema.content.retrieve:handler` specification.

#### Inputs:

* `$source` is a URI identifying the location from which content is to be
  retrieved. Supports the following retrieval protocols / URI schemes:
  * `file`: processes each file matching the given `$source` URI with the
    given processing `$service`. The file component of the URI can
    include wildcards or globs (such as `*.txt` or `*.j?r`) for matching
    multiple files at once.

    The following example would process all `*.txt` files in the
    specified directory:

        file:////server:port/directory/*.txt

    To ensure each file processed is not locked or being written to by
    another process, the file is first moved to a working directory. The
    name of this directory can be configured by adding a query string
    parameter called working to the URI, for example:

        file:////server:port/directory/*.txt?working=temp

    In this example, files are first moved to a subdirectory named
    `temp`. If not specified, the working directory defaults to a
    subdirectory named `working`.

    After successful processing, the file is then moved to an archive
    directory. The name of this directory can be configured by adding a
    query string parameter called archive to the URI, for example:

        file:////server:port/directory/*.txt?archive=backup

    In this example, files are moved to a subdirectory named `backup`
    after being successfully processed. If not specified, the archive
    directory defaults to a subdirectory named `archive`.

    Optionally, archived files older than a given age can be cleaned up
    automatically by the retrieve process by specifying a query string
    parameter called `purge` with an XML duration value representing the
    age an archived file must be before being purged, for example:

        file:////server:port/directory/*.txt?purge=P14D

    In this example, any files in the archive directory older than 14
    days will be automatically deleted by the retrieve process. If the
    query string parameter `purge` is not specified, archived files will
    not be automatically cleaned up.
* `$service` is the fully-qualified name of the content processing
  service, which implements the
  `tundra.schema.content.retrieve:processor` specification, invoked to
  process each item of content retrieved from the `$source` URI.
* `$limit` is an optional maximum number of content matches to be
  processed in a single execution. Defaults to 1000, if not specified.

---

### tundra.content.retrieve:file

The file protocol handler for the `Tundra/tundra.content:retrieve`
service, which retrieves file content for files matching the given
`$source` URI and calls the given `$service` content processing service
to process each file.

Implements the `Tundra/tundra.schema.content.retrieve:handler`
specification.

---

### tundra.content:split

One-to-many conversion of content in one format to another format. Calls
the given splitting service, passing the parsed `$content` as an input,
and emitting the split list of `$contents` as output.

Parser implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:parse`
* Flat File: `WmFlatFile/pub.flatFile:convertToValues`
* [JSON]: `Tundra/tundra.json:parse`
* Pipe separated values: `Tundra/tundra.csv:parse`
* [TSV]: `Tundra/tundra.csv:parse`
* [XML]: `WmPublic/pub.xml:xmlStringToXMLNode`, `pub.xml:xmlNodeToDocument`
* [YAML]: `Tundra/tundra.yaml:parse`

Emitter implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:emit`
* Flat File: `WmFlatFile/pub.flatFile:convertToString`
* [JSON]: `Tundra/tundra.json:emit`
* Pipe separated values: `Tundra/tundra.csv:emit`
* [TSV]: `Tundra/tundra.csv:emit`
* [XML]: `WmPublic/pub.xml:documentToXMLString`
* [YAML]: `Tundra/tundra.yaml:emit`

#### Inputs:

* `$content` is a string, byte array, or input stream of content to be
  split.
* `$service` is the fully-qualified name of the splitting service, which
  accepts a single IData document and returns an IData document list,
  called to split the parsed `$content`.
* `$pipeline` is an optional IData document containing arbitrary
  variables to be included in the input pipeline of the invocation of
  `$service`.
* `$content.type.input` is the MIME media type that describes the format
  of the given `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$content.type.output` is the MIME media type that describes the
  format of the resulting serialized split content, if all split content
  formats are alike:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Alternatively, it is permissible for the resulting list returned by
  `$service` to contain unlike documents (documents whose MIME types are
  different), and in this case `$service` is required to return a string
  list `$content.types`, where each item in `$content.types` has a value
  appropriate (for example, "application/json" for [JSON] content) for
  serializing the corresponding indexed item in the returned document
  list.
* `$namespace.input` is a list of namespace prefixes and the URIs they
  map to, used when parsing [XML] content with elements in one or more
  namespaces.
* `$namespace.output` is a list of namespace prefixes and the URIs they
  map to, used when emitting [XML] content with elements in one or more
  namespaces.

  Alternatively, it is permissible for the resulting list returned by
  `$service` to contain unlike documents (documents whose MIME types are
  different), and in this case `$service` is required to return an IData
  document list `$namespaces`, where each item in `$namespaces` declares the
  namespace prefixes and URIs appropriate for serializing the
  corresponding indexed item in the returned document list.
* `$schema.input` is the fully-qualified name of the parsing schema to
  use when parsing `$content` as [XML] or Flat File content, and can
  have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to parsing `$content` as [XML], if neither
  `$content.type.input` nor `$schema.input` are specified.
* `$schema.output` is the fully-qualified name of the parsing schema to
  use when serializing the split documents to [XML] or Flat File
  content, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Alternatively, it is permissible for the resulting list returned by
  `$service` to contain unlike documents (documents whose formats are
  different), and in this case `$service` is required to return a string
  list `$schemas`, where each item in `$schemas` has a value appropriate
  (document reference for [XML], flat file schema for Flat Files) for
  serializing the corresponding indexed item in the returned document
  list.

  Defaults to serializing the returned documents as [XML], if neither
  `$content.type.output` nor `$schema.output` are specified, and neither
  `$content.types` nor `$schemas` are returned by `$service`.
* `$service.input` is an optional variable name to use in the input
  pipeline of the call to `$service` for the parsed `$content` IData
  document. Defaults to `$document`.
* `$service.output` is an optional variable name used to extract the
  output IData document list from the output pipeline of the call to
  `$service`. Defaults to `$documents`.
* `$encoding.input` is an optional character set used to decode the text
  data if `$content` is provided as a byte array or input stream.
  Defaults to [UTF-8].
* `$encoding.output` is an optional character set used to encode the
  split text datum if the specified `$mode.output` is a byte array or
  stream. Defaults to [UTF-8].
* `$mode.output` is an optional choice of stream, bytes, or string which
  specifies the type of object each item in `$contents` is returned as.
  Defaults to stream.

#### Outputs:

* `$contents` is the resulting list of split content as a string, byte
  array, or input stream, depending on the `$mode.output` chosen.
* `$content.types` is the optional list of MIME media types returned by
  `$service` if the `$contents` list contains unlike media types.
* `$schemas` is the optional list of fully-qualified document references
  (for XML) or flat file schemas (for flat files) returned by `$service`
  if the `$contents` list contains unlike formats.

---

### tundra.content:translate

One-to-one conversion of content in one format to another format. Calls the
given translation service, passing the parsed content as an input, and
emitting or serializing the translated content as output.

Parser implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:parse`
* Flat File: `WmFlatFile/pub.flatFile:convertToValues`
* [JSON]: `Tundra/tundra.json:parse`
* Pipe separated values: `Tundra/tundra.csv:parse`
* [TSV]: `Tundra/tundra.csv:parse`
* [XML]: `WmPublic/pub.xml:xmlStringToXMLNode`, `pub.xml:xmlNodeToDocument`
* [YAML]: `Tundra/tundra.yaml:parse`

Emitter implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:emit`
* Flat File: `WmFlatFile/pub.flatFile:convertToString`
* [JSON]: `Tundra/tundra.json:emit`
* Pipe separated values: `Tundra/tundra.csv:emit`
* [TSV]: `Tundra/tundra.csv:emit`
* [XML]: `WmPublic/pub.xml:documentToXMLString`
* [YAML]: `Tundra/tundra.yaml:emit`

#### Inputs:

* `$content` is a string, byte array, or input stream containing content
  to be translated to another format.
* `$service` is the fully-qualified name of the translation service,
  which accepts a single IData document and returns a single IData
  document, called to translate the parsed `$content`.
* `$content.type.input` is the MIME media type that describes the format
  of the given `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$content.type.output` is the MIME media type that describes the
  format of the resulting serialized translated content:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.
* `$namespace.input` is a list of namespace prefixes and the URIs they
  map to, used when parsing [XML] content with elements in one or more
  namespaces.
* `$namespace.output` is a list of namespace prefixes and the URIs they
  map to, used when emitting [XML] content with elements in one or more
  namespaces.
* `$schema.input` is the fully-qualified name of the parsing schema to
  use when parsing `$content` as [XML] or Flat File content, and can
  have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to parsing `$content` as [XML], if neither
  `$content.type.input` nor `$schema.input` are specified.
* `$schema.output` is the fully-qualified name of the parsing schema to
  use when serializing the translated content to [XML] or Flat File
  content, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Defaults to serializing the translated content as [XML], if neither
  `$content.type.output` nor `$schema.output` are specified.
* `$service.input` is an optional variable name to use in the input
  pipeline of the call to `$service` for the parsed `$content` IData
  document. Defaults to `$document`.
* `$service.output` is an optional variable name used to extract the
  output IData document from the output pipeline of the call to
  `$service`. Defaults to `$translation`.
* `$encoding.input` is an optional character set used to decode the text
  data if `$content` is provided as a byte array or input stream.
  Defaults to [UTF-8].
* `$encoding.output` is an optional character set used to encode the
  translated text data if the specified `$mode.output` is a byte array or
  stream. Defaults to [UTF-8].
* `$mode.output` is an optional choice of stream, bytes, or string which
  specifies the type of object `$translation` is returned as. Defaults to
  stream.

#### Outputs:

* `$translation` is the translated content returned as a string, byte
  array or input stream, depending on the `$mode.output` chosen.

---

### tundra.csv:emit

Serializes an IData document containing a list of records as a [CSV]
formatted string, byte array, or input stream.

#### Inputs:

* `$document` is the IData document containing a list of records to be
  serialized as a [CSV] string, byte array, or input stream.
  * `recordWithNoID` is the IData[] document list of records to be
    serialized.
* `$delimiter` is the character to use to delimit fields in the resulting
  serialization. Defaults to ',' (comma), if not specified.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to [UTF-8].
* `$mode` is an optional choice of {stream, bytes, string} which
  specifies the type of object `$content` is returned as. Defaults to
  stream.

#### Outputs:

* `$content` is the resulting serialization of the records in `$document`
  as [CSV] content.

---

### tundra.csv.mime.type:check

Returns true if the given MIME media type is recognized as a comma, pipe,
or tab separated values media type.

#### Inputs:

* `$content.type` is the MIME media type to be checked.

#### Outputs:

* `$csv?` is a boolean which when true indicates that the given
  `$content.type` is a recognized [CSV] media type.
* `$psv?` is a boolean which when true indicates that the given
  `$content.type` is a recognized pipe separated values media type.
* `$tsv?` is a boolean which when true indicates that the given
  `$content.type` is a recognized [TSV] media type.

---

### tundra.csv:parse

Parses [CSV] content specified as a string, byte array, or input stream
into an `IData` document containing a list of records.

#### Inputs:

* `$content` is a string, byte array, or input stream containing [CSV]
  content to be parsed.
* `$delimiter` is the character used to delimit fields in the given
  `$content`. Defaults to `,` (comma), if not specified.
* `$encoding` is an optional character set to use when `$content` is
  provided as a byte array or input stream to decode the contained text
  data. Defaults to [UTF-8].

#### Outputs:

* `$document` is the resulting `IData` document containing a list of
  records representing the parsed `$content`.
  * `recordWithNoID` is the resulting `IData[]` document list of records.

---

### tundra.datetime:add

Adds a duration of time to the given datetime, formatted according to
the given patterns.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime` is the datetime string to add the duration to.
* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  conforms to. Defaults to an [ISO8601] XML datetime.
* `$duration` is the duration to be added to `$datetime`.
* `$duration.pattern` is an optional duration pattern that `$duration`
  conforms to. Defaults to an [ISO8601] XML duration.

#### Outputs:

* `$datetime` is the resulting datetime with the added duration.

---

### tundra.datetime:compare

Compares two datetime strings, formatted according to the given pattern,
indicating their position in time relative to one another.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime.x` is the datetime string to be compared to $datetime.y.
* `$datetime.y` is the datetime string to be compared to $datetime.x.
* `$pattern` is an optional datetime pattern that `$datetime.x` and
  `$datetime.y` conform to. Defaults to an [ISO8601] XML datetime.

#### Outputs:

* `$before?` is a boolean flag indicating if `$datetime.x` is an earlier
  instant in time than `$datetime.y`.
* `$equal?` is a boolean flag indicating if `$datetime.x` and
  `$datetime.y` represent the same instant in time.
* `$after?` is a boolean flag indicating if `$datetime.x` is a later
  instant in time than `$datetime.y`.

---

### tundra.datetime:concatenate

Concatenates individual date and time strings into a single datetime
string.

#### Inputs:

* `$date` is an [ISO8601] XML date string.
* `$time` is an [ISO8601] XML time string.

#### Outputs:

* `$datetime` is an [ISO8601] XML datetime string that concatenates the
  inputs.

---

### tundra.datetime:duration

Returns the duration between two datetimes.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime.start` is the datetime string representing the starting
  instant for calculating the duration of time.
* `$datetime.end` is the datetime string representing the ending instant
  for calculating the duration of time.
* `$datetime.pattern` is an optional datetime pattern that
  `$datetime.start` and `$datetime.end` conform to. Defaults to an
  [ISO8601] XML datetime.
* `$duration.pattern` is an optional duration pattern that the output
  `$duration` will be formatted as. Defaults to an [ISO8601] XML
  duration.

#### Outputs:

* `$duration` is the duration of time between `$datetime.start` and
  `$datetime.end`, formatted according to the given `$duration.pattern`.

---

### tundra.datetime:earlier

Subtracts a duration of time from the current datetime, formatted according
to the given pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  is formatted as. Defaults to an [ISO8601] XML datetime.
* `$duration` is the duration to be subtracted from the current datetime.
* `$duration.pattern` is an optional duration pattern that `$duration`
  conforms to. Defaults to an [ISO8601] XML duration.
* `$timezone` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost time
  zone identifying the time zone the returned `$datetime` will be
  formatted with.

#### Outputs:

* `$datetime` is the current datetime minus the given `$duration` in the
  given `$timezone`.

---

### tundra.datetime:emit

Returns the given [java.util.Date] object as a string formatted according
to the given datetime pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime.object` is the [java.util.Date] to be formatted as a
  datetime string.
* `$pattern` is an optional datetime pattern that will be used to format
  the resulting `$datetime` string. Defaults to an [ISO8601] XML
  datetime.
* `$timezone` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost time
  zone identifying the time zone the returned `$datetime` will be
  formatted with.

#### Outputs:

* `$datetime` is the [java.util.Date] object formatted as a string
  according to the given `$pattern` in the given `$timezone`.

---

### tundra.datetime:format

Formats a datetime string that conforms to the input pattern, according to
the output pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime` is the datetime string to be reformatted to a different
  pattern.
* `$pattern.input` is an optional datetime pattern that `$datetime`
  conforms to, that will be used to parse the datetime string. Defaults
  to an [ISO8601] XML datetime.
* `$patterns.input` is an optional list of datetime patterns that `$datetime`
  might conform to, useful when the exact pattern is not known. A parse is
  attempted for each pattern until the first successful parse, or until
  all patterns have been tried in which case an unparseable datetime
  exception will be thrown.
* `$pattern.output` is an optional datetime pattern that will be used to
  format the resulting `$datetime` string. Defaults to an [ISO8601] XML
  datetime.
* `$timezone.input` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost
  time zone identifying the time zone the input `$datetime` will
  be parsed with. If specified, this time zone will override any
  zone specified in the `$datetime` string itself.
* `$timezone.output` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost
  time zone identifying the time zone the returned `$datetime` will
  be formatted with.

#### Outputs:

* `$datetime` is the datetime formatted as a string according to the
  given `$pattern.output` in the given `$timezone.output`.

---

### tundra.datetime:later

Adds a duration of time to the current datetime, formatted according
to the given pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  is formatted as. Defaults to an [ISO8601] XML datetime.
* `$duration` is the duration to be added to the current datetime.
* `$duration.pattern` is an optional duration pattern that `$duration`
  conforms to. Defaults to an [ISO8601] XML duration.
* `$timezone` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost time
  zone identifying the time zone the returned `$datetime` will be
  formatted with.

#### Outputs:

* `$datetime` is the current datetime plus the given `$duration` in
  the given `$timezone`.

---

### tundra.datetime:maximum

Returns the largest datetime from the given list of datetime strings.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

#### Inputs:

* `$datetimes` is a list of datetime strings.
* `$pattern` is an optional datetime pattern that each string in the
  `$datetimes` list conforms to, and will be used to parse the datetime
  strings. Defaults to an [ISO8601] XML datetime.


#### Outputs:

* `$datetime` is the largest datetime from the given `$datetimes` list.

---

### tundra.datetime:minimum

Returns the smallest datetime from the given list of datetime strings.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

#### Inputs:

* `$datetimes` is a list of datetime strings.
* `$pattern` is an optional datetime pattern that each string in the
  `$datetimes` list conforms to, and will be used to parse the datetime
  strings. Defaults to an [ISO8601] XML datetime.


#### Outputs:

* `$datetime` is the smallest datetime from the given `$datetimes` list.

---

### tundra.datetime:now

Returns the current datetime formatted according to the given pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$pattern` is an optional datetime pattern that will be used to format
  the resulting `$datetime` string. Defaults to an [ISO8601] XML
  datetime.
* `$timezone` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost time
  zone identifying the time zone the returned `$datetime` will be
  formatted with.

#### Outputs:

* `$datetime` is the current datetime formatted as a string according to
  the given `$pattern` in the given `$timezone`.

---

### tundra.datetime:parse

Parses the given datetime string according to the given pattern, and
returns the resulting [java.util.Date] object.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime` is the datetime string to be parsed.
* `$pattern` is an optional datetime pattern that `$datetime` conforms
  to, and will be used to parse the datetime string. Defaults to an
  [ISO8601] XML datetime.
* `$patterns` is an optional list of datetime patterns that `$datetime` might
  conform to, useful when the exact pattern is not known. A parse is
  attempted for each pattern until the first successful parse, or until
  all patterns have been tried in which case an unparseable datetime
  exception will be thrown.
* `$timezone.input` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost
  time zone identifying the time zone the input `$datetime` will
  be parsed with. If specified, this time zone will override any
  zone specified in the `$datetime` string itself.

#### Outputs:

* `$datetime.object` is a [java.util.Date] object representing the same
  instant in time as the given `$datetime` string.

---

### tundra.datetime:subtract

Subtracts a duration of time from the given datetime, formatted according
to the given patterns.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime` is the datetime string to subtract the duration from.
* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  conforms to. Defaults to an [ISO8601] XML datetime.
* `$duration` is the duration to be subtracted from `$datetime`.
* `$duration.pattern` is an optional duration pattern that `$duration`
  conforms to. Defaults to an [ISO8601] XML duration.

#### Outputs:

* `$datetime` is the resulting datetime with the subtracted duration.

---

### tundra.datetime:validate

Returns true if the given datetime string conforms to the given pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

#### Inputs:

* `$datetime` is the datetime string to be validated.
* `$pattern` is an optional datetime pattern that `$datetime` is
  required to conform to. Defaults to an [ISO8601] XML datetime.
* `$raise?` is an optional boolean flag indicating if an exception
  should be thrown if `$datetime` is found not to conform to `$pattern`.
  Defaults to false.

#### Outputs:

* `$valid?` is a boolean flag indicating the given `$datetime` conforms
  to, and can be parsed by, the given `$pattern`.

---

### tundra.decimal:absolute

Returns the absolute value of the given decimal.

#### Inputs:

* `$decimal` is a signed decimal value.

#### Outputs:

* `$decimal` is the given decimal value unsigned.

---

### tundra.decimal:add

Adds the given decimals, returning the result optionally rounded
to the given precision (number of decimal places) using the
given [rounding algorithm].

#### Inputs:

* `$decimals` is a list of decimal values to be added.
* `$decimal` is a single decimal value to be added.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the result of adding the given decimal values
  and rounding to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:average

Returns the average value of the given list of decimals, optionally
rounded to the given precision (number of decimal places) using the
given [rounding algorithm].

#### Inputs:

* `$decimals` is a list of decimal values to average.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the result of averaging the given list of decimals
  and rounding to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:divide

Divides the given decimal x by y, returning the result optionally
rounded to the given precision (number of decimal places) using
the given [rounding algorithm].

#### Inputs:

* `$decimal.x` is the numerator decimal for the division.
* `$decimal.y` is the denominator decimal for the division.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the result of dividing `$decimal.x` by `$decimal.y`
  and rounding to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:emit

Returns an string representation of the given decimal object.

#### Inputs:

* `$object` is either a `java.math.BigDecimal`, `java.math.BigInteger`,
  `java.lang.Double`, `java.lang.Float`, `java.lang.Integer`, or
  `java.lang.Long` object.

#### Outputs:

* `$string` is the resulting string representation of the decimal.

---

### tundra.decimal:maximum

Returns the maximum value in the given list of decimals, optionally
rounded to the given precision (number of decimal places) using the
given [rounding algorithm].

#### Inputs:

* `$decimals` is a list of decimal values.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the largest value in the given list of decimals,
  rounded to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:minimum

Returns the minimum value in the given list of decimals, optionally
rounded to the given precision (number of decimal places) using the
given [rounding algorithm].

#### Inputs:

* `$decimals` is a list of decimal values.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the smallest value in the given list of decimals,
  rounded to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:multiply

Multiplies the given decimals, returning the result optionally
rounded to the given precision (number of decimal places) using
the given [rounding algorithm].

#### Inputs:

* `$decimals` is a list of decimal values to multiply.
* `$decimal` is a single decimal value to multiply.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is result of multiplying the given decimal values
  and rounding to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:negate

Returns the negative value of the given decimal (-x).

#### Inputs:

* `$decimal` is a decimal value.

#### Outputs:

* `$decimal` is the given decimal value multiplied
  by minus one.

---

### tundra.decimal:parse

Returns an decimal object by parsing the given string.

#### Inputs:

* `$string` is a string representation of a decimal number, formatted
  according to [java.math.BigDecimal grammar].
* `$class` is an optional Java class name that determines the type of
  object returned, a choice of either `java.math.BigDecimal`,
  `java.math.BigInteger`, `java.lang.Double`, `java.lang.Float`,
  `java.lang.Integer`, or `java.lang.Long`. Defaults to `java.math.BigDecimal`,
  if not specified.

#### Outputs:

* `$object` is the resulting Java object representing the parsed decimal.

---

### tundra.decimal:power

Raises the given decimal to the power of the given exponent (d^e),
optionally rounded to the given precision (number of decimal places)
using the given [rounding algorithm].

#### Inputs:

* `$decimal` is a decimal value.
* `$exponent` is the value to raise the decimal by.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is result of raising the given decimal by the
  given exponent, and rounding to the given precision
  using the nominated [rounding algorithm].

---

### tundra.decimal:round

Rounds the given decimal to given precision (number of decimal
places) using the given [rounding algorithm].

#### Inputs:

* `$decimal` is a decimal value to be rounded.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is the given decimal value rounded to the given
  precision using the nominated [rounding algorithm].

---

### tundra.decimal:subtract

Subtracts the given decimal y from x, returning the result
optionally rounded to the given precision (number of decimal places)
using the given [rounding algorithm].

#### Inputs:

* `$decimal.x` is a decimal value.
* `$decimal.y` is a decimal value.
* `$precision` is an optional number of decimal places to be
  preserved in the result.
* `$rounding` is an optional choice of the [rounding algorithm]
  to use when rounding the result to the specified `$precision`.
  If not specifed, defaults to the `HALF_UP` algorithm.
  * `HALF_UP` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case rounds up.
  * `CEILING` rounds towards positive infinity.
  * `DOWN` rounds towards zero.
  * `FLOOR` rounds towards negative infinity.
  * `HALF_DOWN` rounds towards "nearest neighbor" unless both
    neighbors are equidistant, in which case round down.
  * `HALF_EVEN` rounds towards the "nearest neighbor" unless both
    neighbors are equidistant, in which case, round towards the
    even neighbor.
  * `UNNECESSARY` asserts that the requested operation has an exact
    result, and hence no rounding is necessary.
  * `UP` rounds away from zero.

#### Outputs:

* `$decimal` is result of subtracting `$decimal.y` from `$decimal.x`
  and rounding to the given precision using the nominated
  [rounding algorithm].

---

### tundra.decimal:validate

Returns true if the given string can be parsed as a decimal.

#### Inputs:

* `$decimal` is a string to be validated as a decimal.
* `$raise?` is an optional boolean indicating if an exception
  should be thrown if the given string is not a valid
  decimal. Defaults to false.

#### Outputs:

* `$valid?` is a boolean indicating if the given string is
  a valid decimal number.

---

### tundra.directory:create

Creates a new directory.

#### Inputs:

* `$directory` is a string specifying a relative or absolute path
  or file: [URI]; all directories on this path will be created.
* `$raise?` is an optional boolean flag indicating if an exception
  should be thrown if the directory already exists. Defaults to
  false.

---

### tundra.directory:exists

Returns true if the given directory exists.

#### Inputs:

* `$directory` is a string specifying a relative or absolute path
  or file: [URI].

#### Outputs:

* `$exists?` is true if `$directory` exists and is a directory.

---

### tundra.directory:join

Returns a new file path [URI], given a parent path and a child path
or file name.

#### Inputs:

* `$parent` is a string specifying a relative or absolute parent path.
* `$child` is a string specifying a relative child path or file name.

#### Outputs:

* `$uri` is a file: [URI] representing the path `$parent`/`$child`.

---

### tundra.directory:list

Lists a directory, optionally filtering based on the given pattern,
which can be either a regular expression (for example, "\w+\.\w+")
or a wildcard expression (for example, "*.txt"), depending on the
selected mode.

#### Inputs:

* `$directory` is a relative or absolute path or file: [URI] to be
  listed.
* `$pattern` is either an optional regular expression pattern, or
  wildcard file glob pattern (depending on the `$mode` selected), used
  to filter the resulting directory listing.
* `$mode` is an optional choice if either 'regex' or 'wildcard', which
  determines the type of `$pattern` specified. Defaults to 'regex'.
* `$recurse?` is an optional boolean flag indicating if subdirectories
  should also be listed recursively. Defaults to false.

#### Outputs:

* `$directories` is a list of all subdirectories, optionally filtered
  to only those items whose name match the given `$pattern`.
* `$files` is a list of all files in the given `$directory`, optionally
  filtered to only those items whose name match the given `$pattern`.

---

### tundra.directory:ls

Returns a raw directory listing with no additional processing: useful for
when performance takes priority over ease of use; for example, when the
directory contains hundreds of thousands or more files.

#### Inputs:

* `$directory` is a relative or absolute path or file: [URI] to be
  listed.

#### Outputs:

* `$list` is a raw list of the names of all items in the directory.

---

### tundra.directory:normalize

Returns the canonical file: URI that represents the given directory.

#### Inputs:

* `$directory` is a relative or absolute path, or file: [URI], to be
  normalized.

#### Outputs:

* `$directory` is the equivalent canonical file: [URI] representing
  the directory.

---

### tundra.directory:purge

Deletes all files older than the given duration, based on the last modified
datetime, from the given directory, and optionally from all sub-
directories.

#### Inputs:

* `$directory` is the directory from which files will be deleted,
  specified as either a relative or absolute file path or file: [URI].
* `$duration` is the duration of time representing the age of files to be
  deleted. For example, a duration of P1D will delete all files that were
  last modified 24 hours ago or earlier.
* `$duration.pattern` is an optional pattern describing the type of
  duration specified by the `$duration` string. Defaults to an [ISO8601]
  XML string.
* `$recurse?` is an optional boolean flag indicating if files in sub-
  directories should also deleted. Defaults to false.

#### Outputs:

* `$count` is the number of files deleted by this service that were older
  than the given `$duration`.

---

### tundra.directory:reflect

Returns useful details about the given directory.

#### Inputs:

* `$directory` is the name of the directory to return details about,
  specified as either a relative or absolute file path or file: [URI].

#### Outputs:

* `$directory.properties` is an IData document containing the following
  details about the given `$directory`:
  * `exists?` is a boolean flag indicating if the given directory exists.
  * `parent` is the canonical file: [URI] that represents the parent
    directory that contains the given directory.
  * `name` is the name component not including the path of the given
    directory.
  * `modified` is the last modified datetime of the given directory.
  * `uri` is the canonical file: [URI] that represents the given
    directory.

---

### tundra.directory:remove

Deletes the given directory, and optionally all child files and
directories recursively if desired.

#### Inputs:

* `$directory` is a relative or absolute path or file: [URI] to be
  deleted.
* `$recurse?` is a boolean flag indicating that all child files and
  directories should be recursively deleted also. If false, and
  the directory is not empty, an exception will be thrown. Defaults
  to false.

---

### tundra.directory:rename

Renames the source directory to the target directory name. If the
target already exists, an exception will be thrown.

#### Inputs:

* `$directory.source` is a relative or absolute path or file: [URI]
  to be renamed.
* `$directory.target` is a relative or absolute path of file: [URI]
  to rename `$directory.source` to.

---

### tundra.document:amend

Edits the given IData `$document` with the list of {key, value} pairs
specified in `$amendments`.

#### Inputs:

* `$document` is the IData document to be amended.
* `$amendments` is an IData document list containing all the edits to be
  made to the given `$document`.
  * `key` is a fully-qualified (for example, `a/b/c[0]`) key identifying
    the value in `$document` to be edited.
  * `value` is the value to be assigned to the item identified by `key`,
    and can include percent-delimited variable substitution strings which
    will be substituted prior to being inserted into the `$document`.
  * `condition` is an optional `Tundra/tundra.condition:evaluate`
    conditional statement, which is evaluated against the pipeline and
    only if the condition evaluates to true will the associated amended
    `value` be applied. If not specified, the amended `value` will always
    be applied.

#### Outputs:

* `$document` is the resulting edited IData document.

---

### tundra.document:blankify

Converts all null values in the given IData document to empty strings.

#### Inputs:

* `$document` is an IData document whose null values are to be converted
  to empty strings.
* `$recurse` is an optional boolean indicating if embedded IData documents
  and IData[] document lists should also have their null values converted
  to empty strings. Defaults to false.

#### Outputs:

* `$document` is the given IData document with all null values converted to
  empty strings.

---

### tundra.document:clear

Removes all elements from the given IData document, except for
any keys specified in the preserve list.

#### Inputs:

* `$document` is an IData document whose keys are to be removed.
* `$preserve` is list of keys which will not be removed from
  the given IData document. Keys can be simple or fully
  qualified, such as `a/b/c[0]/d`.

#### Outputs:

* `$document` is the given IData document with all keys removed,
  except for those specified in `$preserve`.

---

### tundra.document:compact

Removes all null values from the given IData document.

#### Inputs:

* `$document` is an IData document from which null values are
  to be removed.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be compacted. Defaults to false.

#### Outputs:

* `$document` is the given IData document with all null values
  removed.

---

### tundra.document:copy

Copies the value associated with the source key to the target key
in the given IData document.

#### Inputs:

* `$document` is an IData document in which to copy the value
  associated with the source key to the target key.
* `$key.source` is a key identifying the value to be copied,
  and can be simple or fully qualified, such as `a/b/c[0]/d`.
* `$key.target` is the key to which the source key value is
  copied, and can be simple or fully qualified, such as
  `a/b/c[0]/d`.

#### Outputs:

* `$document` is the given IData document where the value
  associated with `$key.source` has been copied to `$key.target`.

---

### tundra.document:deliver

Serializes the given IData document, and delivers it to the given
destination URI.

Additional delivery protocols can be implemented by creating a service
named for the URI scheme in the folder `tundra.content.deliver`. Services
in this folder should implement the `tundra.schema.content.deliver:handler`
specification.

#### Inputs:

* `$document` is the IData document to be serialized and delivered to the
  given destination URI.
* `$destination` is a URI identifying the location where the serialized
  document should be delivered. Supports the following delivery protocols
  (URI schemes):
  * `file`: writes the given content to the file specified by the
    destination URI. The following additional options can be provided
    via the `$pipeline` document:
    * `$mode`: append / write
  * `ftp`: uploads the given content to the FTP server, directory and
    file specified by the destination URI. An example FTP URI is as
    follows:

        ftp://aladdin:opensesame@example.com:21/path/file?append=true&active=true&ascii=true

    The following additional options can be provided via the `$pipeline`
    document:
    * `$user` is the username used to log in to the FTP server. Defaults
      to the username specified in the authority section of the URI, if
      not specified.
    * `$password` is the password used to log in to the FTP server.
      Defaults to the password specified in the authority section of the
      URI, if not specified.
    * `$active` is a boolean which when true indicates that the
      connection to the FTP server should be in active mode. Defaults to
      false (passive mode), if not specified.
    * `$append` is a boolean which when true will append the given
      content to the file, rather than overwrite it, if the file already
      exists. Defaults to false (overwriting), if not specified.
    * `$ascii` is a boolean which when true indicates that the file
      transfer should be made in ascii mode. Defaults to false (binary
      mode), if not specified.
    * `$timeout` is an optional XML duration string which specifies how
      long the client waits for a response from the server before timing
      out and terminating the request with an error. Defaults to PT60S,
      if not specified.
  * `http`: transmits the given content to the destination URI. The
    following additional options can be provided via the `$pipeline`
    document:
    * `$method`: get / put / post / delete / head / trace / options
    * `$headers/*`: additional HTTP headers as required
    * `$authority/user`: the username to log on to the remote web server
    * `$authority/password`: the password to log on to the remote web
      server
  * `https`: refer to http
  * `jms`: sends the given content as a [JMS] [javax.jms.BytesMessage] to
    the specified [JMS] alias and queue or topic. The following
    additional settings can be specified:
    * $headers/*: additional properties to be added to the [JMS] message
      header, which can be used for filtering by [JMS] subscribers.

    The following example will deliver the given content as a [JMS] bytes
    message to the JMS alias DEFAULT_IS_JMS_CONNECTION, [JMS] topic
    JMS::Temporary::Topic, with a time to live of 1 day, and with the
    default priority of 4:

        jms://DEFAULT_IS_JMS_CONNECTION?topic=JMS::Temporary::Topic&lifetime=P1D

    The following example will deliver the given content as a [JMS] bytes
    message to the [JMS] alias DEFAULT_IS_JMS_CONNECTION, [JMS] queue
    JMS::Temporary::Queue, with no expiry, and with the specified
    priority of 1:

        jms://DEFAULT_IS_JMS_CONNECTION?queue=JMS::Temporary::Queue&priority=1

  * `mailto`: sends an email with the given content attached. An example
    mailto URI is as follows:

        mailto:bob@example.com?cc=jane@example.com&subject=Example&body=Example&attachment=message.xml

    The following additional override options can be provided via the
    $pipeline document:
    * `$attachment`: the attached file's name
    * `$from`: email address to send the email from
    * `$subject`: the subject line text
    * `$body`: the main text of the email
    * `$smtp`: an SMTP URI specifying the SMTP server to use (for
      example, `smtp://user:password@host:port`), defaults to the SMTP
      server configured in the Integration Server setting
      `watt.server.smtpServer`.
  * `sap+idoc`: sends an IDoc XML message to an SAP system. Both opaque
    and non-opaque URIs are allowed: opaque URIs are useful if the SAP
    Adapter alias contains characters not permitted in a normal domain
    name, such as underscores.

    An example opaque sap+idoc URI is as follows, where sap_r3 is the
    SAP Adapter alias name, and the user and password are provided as
    query string parameters:

        sap+idoc:sap_r3?user=aladdin&password=opensesame&client=200&language=en&queue=xyz

    An example non-opaque sap+idoc URI is as follows, where sappr3 is the
    SAP Adapter alias name, and the user and password are provided in the
    authority section of the URI:

        sap+idoc://aladdin:opensesame@sappr3?client=200&languange=en&queue=xyz

    The following additional override options can be provided via the
    `$pipeline` document, and if specified will overrided the relevant
    parts of the destination URI:
    * `$user` is the username used for the SAP session. Defaults to the
      SAP Adapter alias username, if not specified.
    * `$password` is the password used for the SAP session. Defaults to
      the SAP Adapter alias password, if not specified.
    * `$client` is the SAP client used for the SAP session. Defaults to
      the SAP Adapter alias client, if not specified.
    * `$language` is the language used for the SAP session. Defaults to
      the SAP Adapter alias language, if not specified.
    * `$queue` is the optional name of the SAP system inbound queue,
      required when using queued remote function calls (qRFC).

* `$content.type` is an optional MIME media type describing the type
  content being delivered.
* `$schema` is an optional input which determines whether to serialize
  the document as [XML], [JSON], Flat File, and can have the following
  values:
  * For [XML] content, specify the fully-qualified name of the document
    reference that defines the [XML] format.
  * For [JSON] content specify the MIME media type "application/json".
  * For Flat File content specify the fully-qualified name of the flat
    file schema that defines the Flat File format.

  Defaults to serializing `$content` as [XML], if no `$schema` is
  specified.
* `$encoding` is an optional character set used to encode the serialized
  document data upon delivery. Defaults to [UTF-8].
* `$pipeline` is an optional IData document for providing arbitrary
  variables to the delivery implementation service.

#### Outputs:

* `$message` is an optional response message, useful for logging, that
  may be returned by specific delivery protocols.
* `$response` is an optional response content returned by the delivery
  (for example, the HTTP response body).
* `$response.type` is an optional MIME media type describing the type of
  `$response` returned.

---

### tundra.document:drop

Removes the element with the given key from the given IData document.

#### Inputs:

* `$document` is an IData document from which to remove the element
  identified by `$key`.
* `$key` is a key identifying the element in `$document` to be removed,
  and can be simple or fully qualified, such as `a/b/c[0]/d`.

#### Outputs:

* `$document` is the given IData document where the element
  associated with `$key` has been removed.

---

### tundra.document:duplicate

Returns an optionally recursive clone of the the given IData
document.

#### Inputs:

* `$document` is an IData document to be cloned.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be cloned. If not cloned, the resulting IData document
  will reference the same IData and IData[] objects as
  the input document. Defaults to false.

#### Outputs:

* `$duplicate` is the cloned input IData document.

---

### tundra.document:each

Iterates over all elements in the given IData document, invoking
the given service for each key value pair.

#### Inputs:

* `$document` is an IData document whose elements are to be
  iterated over.
* `$service` is the fully-qualifed name of the service to
  be called to process each element .
* `$pipeline` is an optional input pipeline for providing
  arbitrary input arguments to `$service`.
* `$key.input` is the optional argument name used when
  passing each iteration's key in the input pipeline of
  each invocation of `$service`. Defaults to $key.
* `$value.input` is the optional argument name used when
  passing each iteration's value in the input pipeline of
  each invocation of `$service`. Defaults to $value.
* `$value.class` is an optional Java class name, which when
  specified restricts the elements iterated to only those
  whose value are an instance of the given class. When
  not specified, all elements in the `$document` are iterated
  over.
* `$recurse` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be iterated over. Defaults to false.

---

### tundra.document:emit

Serializes the given IData document as an [IData XML] string,
byte array, or input stream.

#### Inputs:

* `$document` is an IData document to be serialized.
* `$encoding` is an optional character set used to encode the
  serialized document when returned as a byte array or input
  stream. Defaults to [UTF-8].
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string', which determines the type of object returned by
  this service. Defaults to 'stream'.

#### Outputs:

* `$content` is the resulting serialized document.

---

### tundra.document:equal

Returns true if the given documents are equal (contain the same set
of keys and values).

#### Inputs:

* `$document.x` is an IData document to be compared to `$document.y`.
* `$document.y` is an IData document to be compared to `$document.x`.

#### Outputs:

* `$equal?` is a boolean indicating if the given documents contain
  the same set of keys and values.

---

### tundra.document:first

Returns the first key value pair from the given IData document.

#### Inputs:

* `$document` is an IData document from which to fetch the first
  element.

#### Outputs:

* `$key` is the key of the first element in the given IData
  document.
* `$value` is the value of the first element in the given IData
  document.

---

### tundra.document:get

Returns the value associated with the given key from the given IData
document.

#### Inputs:

* `$document` is an IData document from which to fetch the first
  element.
* `$key` is the key identifying the value in the given document to
  be returned, and can be simple or fully qualified, such as
  `a/b/c[0]/d`.
* `$default.object` is an optional value to be returned when either
  the given `$key` does not exist or its associated value is null.
* `$default.string` is an optional string value, provided for
  convenience when hard-coding a default value, to be returned
  when either the given `$key` does not exist or its associated
  value is null.

#### Outputs:

* `$value` is the value associated with the given `$key` in the given
  IData document, or the given `$default.object` or `$default.string`
  if specified and the associated value for `$key` either does not
  exist or is null.

---

### tundra.document.key:lowercase

Converts all keys in the given IData document to lower case.

#### Inputs:

* `$document` is an IData document whose keys are to be
  converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their keys converted to lower case. Defaults to
  false.

#### Outputs:

* `$document` is the given IData document with all keys
  converted to lower case.

---

### tundra.document.key:normalize

Converts all keys in the given IData document to legal Java
identifiers by replacing all illegal characters with
underscore characters.

#### Inputs:

* `$document` is an IData document whose keys are to be
  converted to legal Java identifiers.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their keys converted to legal Java identifiers.
  Defaults to false.

#### Outputs:

* `$document` is the given IData document with all keys
  converted to to legal Java identifiers.

---

### tundra.document.key:replace

Replaces all occurrences of the given [regular expression pattern]
in each key in the given IData document with the replacement
string.

#### Inputs:

* `$document` is an IData document to have all occurrences of the
  given [regular expression pattern] in each key replaced.
* `$pattern` is the [regular expression pattern] to match against
  each key.
* `$replacement` is the replacement string to be substituted in
  each key wherever the given pattern is found.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have occurrences of the pattern in their string values
  replaced. Defaults to false.

#### Outputs:

* `$document` is the given IData document with all occurrences of
  the given [regular expression pattern] in each key replaced
  with `$replacement`.

---

### tundra.document.key:trim

Removes leading and trailing whitespace from all keys in the given
IData document.

#### Inputs:

* `$document` is an IData document whose keys are to be trimmed of
  leading and trailing whitespace characters.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their keys trimmed. Defaults to false.

#### Outputs:

* `$document` is the given IData document with all keys trimmed of
  leading and trailing whitespace characters.

---

### tundra.document.key:uppercase

Converts all keys in the given IData document to upper case.

#### Inputs:

* `$document` is an IData document whose keys are to be
  converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their keys converted to upper case. Defaults to
  false.

#### Outputs:

* `$document` is the given IData document with all keys
  converted to upper case.

---

### tundra.document:keys

Returns the top-level keys in the given IData document that match
the given regular expression pattern if specified, or all top-level
keys if no pattern is specified.

#### Inputs:

* `$document` is an IData document from which all top-level
  keys are to be fetched.
* `$pattern` is an optional [regular expression pattern] that
  is used to filter the list of keys returned. If not specified,
  all keys are returned.

#### Outputs:

* `$keys` is the list of the top-level keys in the given IData
  document that match the given regular expression `$pattern` if
  specified, or all top-level keys if no `$pattern` was specified.

---

### tundra.document:last

Returns the last key value pair from the given IData document.

#### Inputs:

* `$document` is an IData document from which to fetch the last
  element.

#### Outputs:

* `$key` is the key of the last element in the given IData
  document.
* `$value` is the value of the last element in the given IData
  document.

---

### tundra.document:length

Returns the number of top-level key value pairs in the given
IData document.

#### Inputs:

* `$document` is an IData document.

#### Outputs:

* `$length` is the number of top-level keys in the given IData
  document.

---

### tundra.document:listify

Converts the value or values identified by the given key to a new
list containing the original value or values as its items, unless
the original value is already a list in which case it remains
unmodified.

#### Inputs:

* `$key` is a simple or fully-qualified (such as `a/b/c[0]/d`) key
  identifying the value to be converted to a list.
* `$scope` is an optional `IData` document that, if specified, is used
  to resolve the given `$key` against. If not specified, `$key` is
  resolved against the pipeline.

#### Outputs:

* `$scope` is returned if an input `$scope` was provided, where the value
  identified by `$key` within it has been converted to a list. If
  no input `$scope` was specified, the value identified by `$key` in the
  pipeline is instead converted to a list. If `$key` does not identify
  any value, this service does nothing.

---

### tundra.document:map

Constructs a new IData document by invoking the given service for
each key value pair in the given IData document, and inserting
the key value pair returned by the service in the resulting IData
document.

This is an implementation of a higher-order [map function] for
IData objects.

For an example of how to use this service, refer to the
tundra.document.key:* and tundra.document.value:* services.

#### Inputs:

* `$document` is an IData document whose elements are to be
  processed by the given `$service`.
* `$service` is the fully-qualifed name of the service to
  be called to process each element, and should return a
  new key value pair as a result.
* `$pipeline` is an optional input pipeline for providing
  arbitrary input arguments to `$service`.
* `$key.input` is the optional argument name used when
  passing each key to `$service`. Defaults to $key.
* `$key.output` is the optional argument name used by `$service`
  when returning the resulting key. Defaults to $key.
* `$value.input` is the optional argument name used when
  passing each value to `$service`. Defaults to $value.
* `$value.output` is the optional argument name used by `$service`
  when returning the resulting value. Defaults to $value.
* `$value.class` is an optional Java class name, which when
  specified restricts the elements processed to only those
  whose value are an instance of the given class. When
  not specified, all elements in the `$document` are processed.
* `$recurse` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be processed. Defaults to false.

#### Outputs:

* `$document` is the resulting IData document created from the
  key value pairs returned by `$service`.

---

### tundra.document:merge

Merges multiple IData documents into a single document. Only top-level
elements are merged, and if duplicate keys exist in the documents being
merged, the latest wins.

This service can be useful for combining a document constructed with
default values with a document sourced externally, where the merged
document will contain the key set union of both documents, and will
include default values where no value was present in the externally
sourced document.

#### Inputs:

* `$documents` is a document list (IData[]) containing IData documents
  to be merged into a single IData document.

#### Outputs:

* `$document` is the merged IData document, containing all keys from all
  documents in the given document list and the latest (in terms of list
  index) values associated with those keys.

---

### tundra.document:normalize

Returns a new IData document with all fully qualified keys (for example,
'a/b/c' or 'x/y[0]/z[1]') deconstructed into their constituent parts, and
any non-IData objects converted to an IData representation wherever
possible.

For example, if the IData document contains the following key value pairs
(using [JSON] notation to represent the pipeline):

    {
      "a/b/c": "example 1",
      "a/b/d": "example 2",
      "e": "example 3",
      "f[0]": "example 4",
      "f[1]": "example 5"
    }

This is normalized to the following:

    {
      "a": {
        "b": {
          "c": "example 1",
          "d": "example 2"
        }
      },
      "e": "example 3",
      "f": ["example 4", "example 5"]
    }

Keys using path-style notation, for example `a/b/c`, are
converted to nested IData documents with the final key
in the path, `c` in this example, assigned the value of
the original key.

Keys using array- or list-style notation, for example `f[0]`,
are converted to an array or list with the value of the
original key assigned to the indexed item (the zeroth item in
this example).

#### Inputs:

* `$document` is an IData document to be normalized.

#### Outputs:

* `$document` is the resulting normalized IData document.

---

### tundra.document:parse

Parses (or deserializes) the given [IData XML] string, byte array, or
input stream to an IData document.

#### Inputs:

* `$content` is a string, byte array, or input stream containing a
  serialized IData document.
* `$encoding` is an optional character set used to decode the
  `$content` when provided as a byte array or input stream. Defaults
  to [UTF-8].

#### Outputs:

* `$document` is the resulting deserialized IData document.

---

### tundra.document:pivot

Returns an IData[] document list of each key value pair in the given IData
document.

#### Inputs:

* `$document` is an IData document to be pivoted.
* `$recurse?` is an optional boolean indicating if embedded IData
  documents and IData[] document lists should also be pivoted. Note that
  a pivoted IData[] document list is returned as a two dimensional IData
  array (IData[][]). Defaults to false, if not specified.

#### Outputs:

* `$pivot` is an IData[] document list where each item in the list
  represents each key value pair present in the given `$document`.

---

### tundra.document:put

Sets the value associated with the given key in the given IData document.

#### Inputs:

* `$document` is an IData document in which to insert the given key
  value pair.
* `$key` is the key to be inserted into the given IData document, and
  can be simple or fully-qualified, such as `a/b/c[0]/d`. If the key
  already exists, it's value will be overwritten with the given value.
* `$value` is the value to be associated with the given key. If not
  specified, a null value will be inserted.

#### Outputs:

* `$document` is the resulting IData document containing the new key
  value pair.

---

### tundra.document:rename

Renames the value associated with the source key to have the target key
in the given IData document.

#### Inputs:

* `$document` is an IData document in which to rename the given key.
* `$key.source` is the key to be renamed, and can be simple or fully-
  qualified, such as `a/b/c[0]/d`.
* `$key.target` is the new name that source key will be renamed to,
  and can be simple or fully-qualified, such as `a/b/c[0]/d`. If the
  target key already exists, its value will be overwitten with the
  value that was associated with the source key.

#### Outputs:

* `$document` is the resulting IData document, where the source key
  has been renamed to the target key.

---

### tundra.document:sort

Sorts the given IData document by its keys in natural ascending order.

#### Inputs:

* `$document` is an IData document to be sorted.
* `$recurse` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be sorted. Defaults to false.

#### Outputs:

* `$document` is the resulting IData document, where the elements have
  been sorted by their keys into natural ascending order.

---

### tundra.document:split

One-to-many conversion of an IData document to an IData[] document list.
Calls the given splitting service, passing the document as an input, and
returning the split list of documents as output.

#### Inputs:

* `$document` is an IData document to be processed by the given splitting
  service.
* `$service` is the fully-qualified name of the splitting service, which
  accepts a single IData document and returns an IData document list,
  called to split `$document`.

  It is permissible for the resulting list returned by `$service` to
  contain unlike documents (documents whose formats are different), and
  in this case `$service` is required to return two string lists:
  * `$content.types` specifies a MIME media type per item in the returned
    list of documents.
  * `$schemas` specifies the fully-qualified document reference (for
    XML) or flat file schema (for Flat Files) per item in the returned
    list of documents, which can be used to serialize that item.
* `$pipeline` is an optional IData document containing arbitrary
  variables to be included in the input pipeline of the invocation of
  `$service`.
* `$service.input` is an optional variable name to use in the input
  pipeline of the call to `$service` for the IData document. Defaults to
  `$document`.
* `$service.output` is an optional variable name used to extract the
  output IData document list from the output pipeline of the call to
  `$service`. Defaults to `$documents`.

#### Outputs:

* `$documents` is the resulting list of IData documents.
* `$content.types` is the list of MIME media types if the `$documents`
  list contains unlike media types.
* `$schemas` is the list of fully-qualified document reference (for XML)
  or flat file schema (for flat files) names, returned by `$service` for
  when the `$documents` list contains unlike formats.

---

### tundra.document:squeeze

Trims all leading and trailing whitespace from all string values, then
converts empty strings, empty IData documents, and empty lists to nulls,
then compacts the IData document by removing all null values.

#### Inputs:

* `$document` is an IData document to be squeezed.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be squeezed. Defaults to `false`.

#### Outputs:

* `$document` is the resulting IData document with all string values
  trimmed of leading and trailing whitespace characters, and all
  empty string values, empty IData documents, empty lists, and null
  values removed.

---

### tundra.document:stringify

Converts all non-IData document and non-IData[] document list values in the
given IData document to strings.

#### Inputs:

* `$document` is an IData document whose values are to be converted to
  strings.
* `$recurse` is an optional boolean indicating if embedded IData documents
  and IData[] document lists should also have their values converted to
  strings. Defaults to `false`.


#### Outputs:

* `$document` is the given IData document with all non-IData document and
  non-IData[] document list values converted to strings.

---

### tundra.document:substitute

Attempts variable substitution on each string value in the given IData
document by replacing all occurrences of substrings matching `%key%` with
the associated (optionally scoped) value.

Optionally replaces null or non-existent values with the given default
value.

#### Inputs:

* `$document` is an IData document to perform variable substitution on.
* `$pipeline` is an optional scope used to resolve key references. If
  not specified, keys are resolved against the pipeline itself.
* `$default` is an optional default value to substitute in place of keys
  that resolve to null or missing values. If not specified, no
  substitution will be made for keys that resolve to null or missing
  values.

#### Outputs:

* `$document` is the resulting IData document with all variable
  substitution patterns in all values, such as `%key%`, replaced with
  the value of the key (resolved against either `$pipeline`, if
  specified, or the pipeline itself).

---

### tundra.document:translate

One-to-one conversion of one IData document to another IData document.
Calls the given translation service, passing the document as an input, and
returning the translated document as output.

#### Inputs:

* `$document` is an IData document to be translated.
* `$service` is the fully-qualified name of the translation service,
  which accepts a single IData document and returns a single IData
  document, called to translate the given `$document`.
* `$pipeline` is an optional IData document for providing arbitrary
  variables to the invocation of $service.
* `$service.input` is an optional variable name to use in the input
  pipeline of the call to `$service` for the given IData document.
  Defaults to `$document`.
* `$service.output` is an optional variable name used to extract the
  output IData document from the output pipeline of the call to
  `$service`. Defaults to `$translation`.

#### Outputs:

* `$translation` is the translated IData document.

---

### tundra.document.value:lowercase

Converts all string values in the given IData document to lower
case.

#### Inputs:

* `$document` is an IData document whose string values are to be
  converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their string values converted to lower case. Defaults
  to `false`.

#### Outputs:

* `$document` is the given IData document with all string values
  converted to lower case.

---

### tundra.document.value:replace

Replaces all occurrences of the given [regular expression pattern]
in each string value in the given IData document with the replacement
string.

#### Inputs:

* `$document` is an IData document to have all occurrences of the
  given [regular expression pattern] in each string value replaced.
* `$pattern` is the [regular expression pattern] to match against
  each string value.
* `$replacement` is the replacement string to be substituted in
  each string value wherever the given pattern is found.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have occurrences of the pattern in their string values
  replaced. Defaults to `false`.

#### Outputs:

* `$document` is the given IData document with all occurrences of
  the given [regular expression pattern] in each string value
  replaced with `$replacement`.

---

### tundra.document.value:trim

Removes leading and trailing whitespace from all string values in the
given IData document.

#### Inputs:

* `$document` is an IData document whose string values are to be
  trimmed of leading and trailing whitespace characters.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their string values trimmed. Defaults to `false`.

#### Outputs:

* `$document` is the given IData document with all string values
  trimmed of leading and trailing whitespace characters.

---

### tundra.document.value:uppercase

Converts all string values in the given IData document to upper case.

#### Inputs:

* `$document` is an IData document whose string values are to be
  converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  have their string values converted to upper case. Defaults to
  `false`.

#### Outputs:

* `$document` is the given IData document with all string values
  converted to upper case.

---

### tundra.document:values

Returns all the top-level values in the given IData document.

#### Inputs:

* `$document` is an IData document from which all top-level
  values are to be fetched.

#### Outputs:

* `$values` is the list of all top-level values in the given IData
  document.

---

### tundra.dns:localhost

Returns the fully-qualifed domain name, host name, and IP address for
the local host.


#### Outputs:

* `$domain` is the fully-qualified domain name of the local host.
* `$host` is the local host name.
* `$ip` is the local IP address.

---

### tundra.dns:resolve

Looks up the given name in the Domain Name System ([DNS]), returning the
fully-qualifed domain name, host name, and IP address, if found.

#### Inputs:

* `$name` is a host name, domain name, or IP address to be resolved
  against [DNS].

#### Outputs:

* `$domain` is the fully-qualified domain name associated with the given
  name.
* `$host` is the host name associated with the given name.
* `$ip` is the IP address associated with the given name.

[DNS]: <http://en.wikipedia.org/wiki/Domain_Name_System>

---

### tundra.duration:add

Adds one duration (x) to another (y), returning (x + y).

#### Inputs:

* `$duration.x` is a duration string to be added to `$duration.y`.
* `$duration.y` is a duration string to be added to `$duration.x`.
* `$pattern.input` is an optional pattern describing the type of
  duration specified by the `$duration.x` and `$duration.y` strings.
  Defaults to an [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format
  the returned `$duration` string. Defaults to an [ISO8601] XML
  string.


#### Outputs:

* `$duration` is a duration string equal to (`$duration.x` +
  `$duration.y`).

---

### tundra.duration:compare

Compares one duration (x) to another (y), returning if the first is
less than, equal to, greater than, or if the comparison is
indeterminate.

Indeterminate comparisons occur when, for example, comparing 1 month
with 30 days: as the result could change depending on the month in
question, it is therefore considered indeterminate.

#### Inputs:

* `$duration.x` is a duration string to be compared to `$duration.y`.
* `$duration.y` is a duration string to be compared to `$duration.x`.
* `$pattern` is an optional pattern describing the type of duration
  specified by the `$duration.x` and `$duration.y` strings. Defaults
  to an [ISO8601] XML string.

#### Outputs:

* `$lesser?` is true if `$duration.x` is a smaller duration than
  `$duration.y`.
* `$equal?` is true if `$duration.x` is equivalent to `$duration.y`.
* `$greater?` is true if `$duration.x` is larger than `$duration.y`.
* `$indeterminate?` is true if `$duration.x` and `$duration.y` cannot be
  compared.

---

### tundra.duration:format

Formats the given duration string according to the desired pattern.

A start instant may be required when formatting fields with
indeterminate values, such as converting months to days (because the
number of days in a month varies).

#### Inputs:

* `$duration` is a duration string to be formatted.
* `$pattern.input` is an optional pattern describing the type of
  duration specified by the `$duration` string. Defaults to an
  [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format the
  `$duration` string. Defaults to an [ISO8601] XML string.
* `$datetime` is an optional datetime string used as a
  starting instant to resolve indeterminate values (such as the
  number of days in a month).
* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  conforms to, that will be used to parse the datetime string.
  Defaults to an [ISO8601] XML datetime.


#### Outputs:

* `$duration` is the duration string formatted according to
  `$pattern.output`.

---

### tundra.duration:multiply

Multiplies the given duration by the given factor.

A start instant may be required when formatting fields with
indeterminate values, such as converting months to days (because the
number of days in a month varies).

#### Inputs:

* `$duration` is an duration string to be multiplied.
* `$pattern.input` is an optional pattern describing the type of
  duration specified by the `$duration` string. Defaults to an
  [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format the
  returned `$duration` string. Defaults to an [ISO8601] XML string.
* `$datetime` is an optional datetime string used as a starting
  instant to resolve indeterminate values (such as the number of
  days in a month).
* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  conforms to, that will be used to parse the datetime string.
  Defaults to an [ISO8601] XML datetime.
* `$factor` is a signed decimal used to multiply the given `$duration`.

#### Outputs:

* `$duration` is the duration string multiplied by the given `$factor`
  and formatted according to `$pattern.output`.

---

### tundra.duration:negate

Reverses the sign of the given duration.

#### Inputs:

* `$duration` is an duration string to be negated.
* `$pattern.input` is an optional pattern describing the type of
  duration specified by the `$duration` string. Defaults to an
  [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format the
  returned `$duration` string. Defaults to an [ISO8601] XML string.


#### Outputs:

* `$duration` is the negated duration string, formatted according to
  `$pattern.output`.

---

### tundra.duration:subtract

Subtracts one duration (x) from another (y), returning (x - y).

#### Inputs:

* `$duration.x` is a duration string to subtract `$duration.y` from.
* `$duration.y` is a duration string to be subtracted from
  `$duration.x`.
* `$pattern.input` is an optional pattern describing the type of
  duration specified by the `$duration.x` and `$duration.y` strings.
  Defaults to an [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format the
  returned `$duration` string. Defaults to an [ISO8601] XML string.

#### Outputs:

* `$duration` is a duration string equal to (`$duration.x` - `$duration.y`),
  and formatted according to `$pattern.output`.

---

### tundra.exception:raise

Throws the given exception, or a new exception with the given message.

#### Inputs:

* `$message` is an optional error message to use when constructing the
  new `com.wm.app.b2b.server.ServiceException` exception object to be
  thrown. If not specified, an empty message will be used to construct
  the exception object.
* `$exception` is an optional existing [java.lang.Throwable] object to be
  thrown. If specified, `$message` is not used.

[java.lang.Throwable]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Throwable.html>

---

### tundra.file:copy

Copies the content of the source file to the target file.

#### Inputs:

* `$file.source` is the name of the file to be copied from, specified
  as either a relative or absolute file path or file: [URI].
* `$file.target` is the name of the file to be copied to, specified as
  either a relative or absolute file path or file: [URI]. If the
  target file already exists, it will be overwritten or appended to,
  depending on the `$mode` selected, with the source file content.
* `$mode` is an optional choice of 'write' or 'append', which determines
  whether the target file will be overwritten or appended to
  respectively. Defaults to 'append', since this is the safer option.

---

### tundra.file:create

Atomically creates a new empty file if a file with this name does not
yet exist.

The check for the existence of the file and the creation of the file
if it does not exist are a single operation that is atomic with respect
to all other file system activities that might affect the file.

#### Inputs:

* `$file` is the name of the file to be created, specified as either a
  relative or absolute file path or file: [URI]. If the file already
  exists, an exception will be thrown. If not specified, a new
  empty temporary file will be created.

#### Outputs:

* `$file` is the name of the file which was created.

---

### tundra.file:executable

Returns true if Integration Server can execute to the given file.

#### Inputs:

* `$file` is the name of the file to test if Integration Server has
  execution permissions, specified as either a relative or absolute
  file path or file: [URI].

#### Outputs:

* `$executable?` is a boolean flag indicating if Integration Server
  has permission to execute the given file. If the file does not
  exist, false is returned.

---

### tundra.file:exists

Returns true if the given file exists.

#### Inputs:

* `$file` is the name of the file to test existence of, specified as
  either a relative or absolute file path or file: [URI].

#### Outputs:

* `$exists?` is a boolean flag indicating if the given file exists.

---

### tundra.file:length

Returns the length of the given file in bytes.

#### Inputs:

* `$file` is the name of the file whose length is to be
  checked, specified as either a relative or absolute
  file path or file: [URI].

#### Outputs:

* `$length` is the length or size in bytes of the given
  file. If the file does not exist, zero is returned.

---

### tundra.file:match

Returns true if the given file name matches the given
regular expression or wildcard pattern.

#### Inputs:

* `$file` is the name of the file to be checked against
  the given `$pattern`, specified as either a relative or
  absolute file path or file: [URI].
* `$pattern` is either a regular expression or wildcard
  pattern, depending on the `$mode` selected, to check
  the file name against
* `$mode` is an optional choice of either 'regex' or
  'wildcard' which determines the type of pattern
  specified. Defaults to 'regex'.

#### Outputs:

* `$match?` is a boolean flag indicating if the file name
  matches the given `$pattern`. Only the file name is
  considered in the match, not the path.

---

### tundra.file:normalize

Returns the canonical file: URI that represents the given
file.

#### Inputs:

* `$file` is the file name to be normalized to the canonical
  file: [URI], specified as either a relative or absolute
  file path or file: [URI].

#### Outputs:

* `$file` is the canonical file: [URI] that represents the
  given file.

---

### tundra.file:process

Provides a safe way of processing a file as a stream, which
is useful for large files or memory constrained environments,
by opening a file for reading, writing, or appending, calling
the given service with the opened file stream object as an
input, and finally closing the stream when done.

As such, the invoked service does not need to open or close the file
stream itself, it only needs to process the opened file stream.
The stream is guaranteed to be closed automatically, regardless
of whether an exception is encountered by the service.

#### Inputs:

* `$file` is the name of the file to be processed, specified
  as either a relative or absolute file path or file: [URI].
* `$mode` is an optional choice of 'read', 'write', or
  'append' which determines whether the file is opened for
  reading, writing, or appending. Defaults to 'read'.
* `$service` is the fully-qualified name of the service which
  will be called to process the opened file.
* `$pipeline` is an optional IData document for specifying
  arbitrary input parameters to the call to `$service`. If
  provided, the call to `$service` is scoped with this IData,
  and will not have access to the global pipeline.
* `$service.input` is an optional input parameter name used
  when adding the opened file stream to the input pipeline
  of the call tor `$service`. Defaults to '$stream'.

#### Outputs:

* `$pipeline` is an optional IData document representing the
  output pipeline of the call to `$service`. This will only
  be returned if an input `$pipeline` was provided. If no
  input `$pipeline` was provided, any outputs from the call
  to `$service` will be merged directly into the global
  pipeline.

---

### tundra.file:read

Reads a file in full, returning the content as either an input
stream, byte array, or string.

As this service reads the entire file into memory, consider
using tundra.file:process instead for large files or in memory
constrained environments.

#### Inputs:

* `$file` is the name of the file to be read, specified as
  either a relative or absolute file path or file: [URI].
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string' which determines how the file contents are
  returned. Defaults to 'stream'.
* `$encoding` is an optional character set to use when reading
  the file as a string. Defaults to [UTF-8].

#### Outputs:

* `$content` is the file contents returned as either an input
  stream, byte array, or string depending on the `$mode`
  selected. Note that even when returned as an input stream,
  the entire file has been read into memory. This mode is
  provided as a convenience only, and since the file contents
  are returned as a [java.io.ByteArrayInputStream] object the
  stream does not need to be explicitly closed as no system
  resources (file handles) are held.

---

### tundra.file:readable

Returns true if Integration Server can read the given file.

#### Inputs:

* `$file` is the name of the file to test if Integration Server has
  read permissions, specified as either a relative or absolute
  file path or file: [URI].

#### Outputs:

* `$readable?` is a boolean flag indicating if Integration Server
  has permission to read the given file. If the file does not
  exist, false is returned.

---

### tundra.file:reflect

Returns useful details about the given file.

#### Inputs:

* `$file` is the name of the file to return details about, specified as
  either a relative or absolute file path or file: [URI].

#### Outputs:

* `$file.properties` is an IData document containing the following
  details about the given `$file`:
  * `exists?` is a boolean flag indicating if the given file exists.
  * `parent` is the canonical file: [URI] that represents the parent
    directory that contains the given file.
  * `name` is the name component not including the path of the given
    file.
  * `base` is the name component not including the path or extension of
    the given file.
  * `extension` is the part of the name after the final period used to
    determine the type of the given file.
  * `type` is the [mime type] associated with the given file.
  * `length` is the length or size in bytes of the given file. If the
    file does not exist, zero is returned.
  * `modified` is the last modified datetime of the given file.
  * `executable?` is a boolean flag indicating if Integration Server has
    permission to execute the given file. If the file does not exist,
    false is returned.
  * `readable?` is a boolean flag indicating if Integration Server has
    permission to read the given file. If the file does not exist, false
    is returned.
  * `writable?` is a boolean flag indicating if Integration Server has
    permission to write or append to the given file. If the file does not
    exist, false is returned.
  * `uri` is the canonical file: [URI] that represents the given file.

---

### tundra.file:remove

Deletes the given file, if it exists. This service does nothing if the
file does not exist (no exception is thrown).

#### Inputs:

* `$file` is the name of the file to be deleted, specified as either a
  relative or absolute file path or file: [URI]. If the file does not
  exists, this service does nothing.

---

### tundra.file:rename

Renames the source file to the target file name.

#### Inputs:

* `$file.source` is the name of the file to be renamed, specified as
  either a relative or absolute file path or file: [URI]. If the source
  file does not exist, an exception will be thrown.
* `$file.target` is the new name for the renamed file, specified as
  either a relative or absolute file path or file: [URI]. If the target
  file already exists, an exception will be thrown.

---

### tundra.file:touch

Updates the modification time of the given file to now, or creates a
new file if it doesn't already exist.

#### Inputs:

* `$file` is the name of the file to be touched, specified as either a
  relative or absolute file path or file: [URI]. If the file does not
  exist, it will be created. If the file does exist, its modification
  time will be updated to current time.

---

### tundra.file:type

Determines the [mime type] for the given file.

Integration Server file extension to [mime type] mappings are defined
in the file ./lib/mime.types. If the [mime type] cannot be found, it
defaults to the [mime type] for arbitrary binary data:

  application/octet-stream

#### Inputs:

* `$file` is the name of the file whose mime type is to be determined,
  specified as either a relative or absolute file path or file: [URI].
  The file is not required to exist, since the mime type is determined
  purely from the file name itself.

#### Outputs:

* `$type` is the [mime type] of the given file.

---

### tundra.file:writable

Returns true if Integration Server can write to the given file.

#### Inputs:

* `$file` is the name of the file to test if Integration Server has
  write permissions, specified as either a relative or absolute
  file path or file: [URI].

#### Outputs:

* `$writable?` is a boolean flag indicating if Integration Server
  has permission to write or append to the given file. If the
  file does not exist, false is returned.

---

### tundra.file:write

Writes or appends the content, provided as a string, byte array or input
stream, to the given file.

If no file is specified, a new temporary file is created automatically.

#### Inputs:

* `$file` is the optional name of the file to which the given content
  is to be written or appended, specified as either a relative or
  absolute file path or file: [URI]. If not specified, a new temporary
  file will be created automatically.
* `$mode` is an optional choice of 'write' or 'append', which determines
  whether the file will be overwritten or appended to respectively.
  Defaults to 'append', since this is the safer option.
* `$content` is a string, byte array, or input stream containing data
  to be written or appended to the given file.
* `$encoding` is an optional character set to use when $content has
  been provided as a string. Defaults to [UTF-8].

#### Outputs:

* `$file` is the name of the file that was written or appended to. If
  no input file name was specified, this is the name of the temporary
  file that was created automatically.

---

### tundra.gzip:compress

Compresses the given content using the [gzip] file compression format.

#### Inputs:

* `$content` is the data to be compressed, specified as a string,
  byte array, or input stream.
* `$encoding` is an optional character set used when `$content` is
  specified as a string. Defaults to [UTF-8].
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string', which determines the type of object returned by
  this service. If the 'string' mode is chosen, the resulting
  gzipped data is base64-encoded. Defaults to 'stream'.

#### Outputs:

* `$content.gzip` is the resulting compressed data in [gzip] format.

---

### tundra.gzip:decompress

Decompresses the given content using the [gzip] file compression format.

#### Inputs:

* `$content.gzip` is the [gzip] compressed data to be decompressed,
  specified as a base64-encoded string, byte array, or input stream.
* `$encoding` is an optional character set used to decode the
  decompressed data when the chosen `$mode` is 'string'. Defaults to
  the [UTF-8].
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string', which determines the type of object returned by
  this service. Defaults to 'stream'.

#### Outputs:

* `$content` is the resulting decompressed data.

---

### tundra.html:decode

[HTML] decodes the given string.

[HTML entities], such as `&lt;` and `&gt;`, are decoded to the
appropriate character representation, such as `<` and `>`.

#### Inputs:

* `$string` is an optional string to be [HTML] decoded.

#### Outputs:

* `$string` is the given string with [HTML entities] decoded.

---

### tundra.html:emit

Serializes an `IData` document as an [HTML] fragment suitable for
embedding in an [HTML] page, formatted as a string, byte array,
or input stream.

Child elements that are IData documents, IData[] document lists,
Object[] object lists, or Object[][] object tables are recursively
serialized to clean [HTML] table elements.

#### Inputs:

* `$document` is the `IData` document to be serialized as an [HTML]
  string, byte array, or input stream.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to
  [UTF-8].
* `$mode` is an optional choice of stream, bytes, or string, which
  specifies the type of object `$content` is returned as. Defaults
  to stream.

#### Outputs:

* `$content` is the resulting serialization of `$document` as [HTML]
  content.

---

### tundra.html:encode

HTML encodes the given string.

Reserved characters in [HTML], such as `<` and `>`, are encoded to the
appropriate [HTML entity], such as `&lt;` and `&gt;`, to ensure the [HTML]
is rendered correctly by web browsers and other [HTML] rendering
software.

#### Inputs:

* `$string` is an optional string to be [HTML] encoded.

#### Outputs:

* `$string` is the given string with special characters [HTML] encoded.

---

### tundra.http:client

Provides an [HTTP] client for issuing requests against [HTTP] servers.

#### Inputs:

* `$request` is an IData document containing the parameters required for
  making an [HTTP] request to an [HTTP] server.
* `$service` is an optional custom [HTTP] response handler service, which
  implements the `tundra.schema.http.response:handler` specification, and
  can be specified when the standard `tundra.http.response:handle`
  service does not suffice. The standard handler does the following:
  * checks the [HTTP response code] is < 400, and throws an exception
    when it is not
  * normalizes the response header keys to lower case
  * converts the response body stream to bytes

#### Outputs:

* `$response` is an IData document containing the [HTTP] server response
  for the given request.

---

### tundra.http.response:accept

A very accepting [HTTP] response handler: accepts any [HTTP response code],
normalizes the response header keys to lower case, and converts the
response body stream to bytes.

#### Inputs:

* `$response` is the [HTTP] response to be processed by this service.

#### Outputs:

* `$response` is the processed [HTTP] response, where the response header
  keys are normalized to lower case, and the response body is returned
  as a byte[] array.

---

### tundra.http.response:handle

Standard [HTTP] response handler: checks that the [HTTP response code] is
< 400, normalizes the response header keys to lower case, and converts the
response body stream to bytes. If the [HTTP response code] is >= 400, an
exception will be thrown.

#### Inputs:

* `$response` is the [HTTP] response to be processed by this service.

#### Outputs:

* `$response` is the processed [HTTP] response, where the
  [HTTP response code] is guaranteed to be < 400, the response header
  keys are normalized to lower case, and the response body is returned
  as a byte[] array.

---

### tundra.http.response:status

Returns the standard message associated with the given [HTTP status code].

#### Inputs:

* `$code` is the [HTTP status code] to return the associated message for.

#### Outputs:

* `$message` is the standard message associated with the given code.

---

### tundra.http.route:clear

Removes all configured custom HTTP routes from the Integration
Server HTTP request dispatcher. Once this service is invoked,
no custom HTTP routes will be in effect.

---

### tundra.http.route:list

Returns a list of all the custom HTTP routes currently in effect.


#### Outputs:

* `$routes` is an `IData[]` document list containing all the directives
  which have been registered with the Integration Server HTTP
  request dispatcher, and the associated custom routing
  instructions.
  * `directive` is the first directory or folder in a URI path, and
    is used by the Integration Server HTTP request dispatcher for
    dispatching requests to the appropriate handler. Note that
    there are a number of built-in directives in Integration
    Server, which are prohibited from being overridden by custom
    routes:
    * `invoke`
    * `soap`
    * `web`
    * `wm-message`
    * `ws`
  * `routes` is an `IData[]` document list of the custom HTTP routing
    instructions associated with this directive in order of
    precedence.
    * `method` is the [HTTP request method] used to match this HTTP
      routing instruction to incoming HTTP requests, and is one of:
      * `get`
      * `put`
      * `post`
      * `head`
      * `connect`
      * `options`
      * `delete`
      * `trace`
    * `uri` is the [URI template] used to match this HTTP
      routing instruction to incoming HTTP requests. Parameters
      specified in the template will be captured against the
      incoming HTTP request URI, and added to the input pipeline
      of the resulting `service` invocation. For example:

          /messages/{client}/{type}

      Will add the variables `client` and `type` to the input pipeline
      of the resulting `service` invocation, with their values
      derived from the matched request URI. A request URI equal to:

          /messages/foo/bar

      will set the input pipeline variables as follows:

          client = "foo"
          type   = "bar"

    * `target` is either the fully-qualified name of a service that
      is invoked, or a URL for a DSP page or static asset in the
      pub directory of a package that will be returned, when an
      incoming HTTP request matches the associated `method` and `uri`.
    * `description` is an optional description of the the HTTP
      routing instruction.
    * `source` is the HTTP route file name from which this routing
      instruction was configured.
  * `routes.length` is the number of routes associated with this
    directive.
* `$routes.length` is the number of directives registered with the
  Integration Server HTTP dispatcher for custom HTTP routing.

---

### tundra.http.route:refresh

Reloads all configured custom HTTP routes from the server- and
package-specific configuration files.

Custom HTTP routes specify an [HTTP request method] and [URI
template] which, if matched by an Integration Server HTTP request,
will return the associated target (either by invoking the target if
it is a service in the same way as the built-in `/invoke` URIs work,
or by returning a DSP page or static asset if the target is not a
service), and can be used to abstract the API used by HTTP clients
from the implementation (unlike `/invoke` URIs or package DSP pages
URIs, which leak the implementation in the URI's path).

Custom HTTP routes can either be configured in a server-specific
configuration file, or a package-specific configuration file.

To register a server-specific custom HTTP route, create or edit the
`<IntegrationServer>/config/http-routes.cnf` file, using the
`<IntegrationServer>/packages/Tundra/config/http-routes.example.cnf`
file as a template.

To register a package-specific custom HTTP route, create or edit the
`<IntegrationServer>/packages/<PackageName>/config/http-routes.cnf`
file, using the
`<IntegrationServer>/packages/Tundra/config/http-routes.example.cnf`
file as a template.

Note that server-specific HTTP routes take precedence over package-specific
routes, and package-specific routes are loaded in lexical package name
order. Routing instructions inside each configuration file must be
specified in order of precedence, and all HTTP routes are aggregated into
a single routing table in Integration Server, and therefore care must be
taken so that routes specified across the various configuration files do
not override each other.

Changes to these HTTP route configuration files do not take effect
until this service is invoked, either manually or by reloading the
Tundra package.

---

### tundra.id:generate

Generates an immutable universally unique identifier ([UUID]). A [UUID]
represents a 128-bit value.

There exist different variants of these global identifiers. The methods
of this class are for manipulating the Leach-Salz variant, although the
constructors allow the creation of any variant of [UUID] (described below).

The layout of a variant 2 (Leach-Salz) [UUID] is as follows: The most
significant long consists of the following unsigned fields:

  0xFFFFFFFF00000000 time_low
  0x00000000FFFF0000 time_mid
  0x000000000000F000 version
  0x0000000000000FFF time_hi

The least significant long consists of the following unsigned fields:

  0xC000000000000000 variant
  0x3FFF000000000000 clock_seq
  0x0000FFFFFFFFFFFF node

The variant field contains a value which identifies the layout of the
[UUID]. The bit layout described above is valid only for a [UUID] with a
variant value of 2, which indicates the Leach-Salz variant.

The version field holds a value that describes the type of this [UUID].
There are four different basic types of UUIDs: time-based, DCE
security, name-based, and randomly generated UUIDs. These types
have a version value of 1, 2, 3 and 4, respectively.

For more information including algorithms used to create UUIDs,
see the Internet-Draft UUIDs and GUIDs or the standards body
definition at ISO/IEC 11578:1996.

#### Inputs:

* `$mode` is an optional choice which determines how the [UUID] is
  converted to a string:
  * `string` returns the [UUID] in the string representation as per
    [RFC 4122], and is the default.
  * `base64` returns the [UUID] as a [Base64] encoded string.

#### Outputs:

* `$id` is the generated [UUID].

---

### tundra.id:normalize

Converts the given string to a legal Java identifier by replacing illegal
characters with underscore characters.

#### Inputs:

* `$string` is a string to be converted to a legal Java identifier.

#### Outputs:

* `$string` is the given string with illegal characters replaced with
  underscore characters.

---

### tundra.integer:absolute

Returns the absolute value of the given integer.

#### Inputs:

* `$integer` is a signed integer.

#### Outputs:

* `$integer` is the absolute (unsigned) value of the given
  integer.

---

### tundra.integer:add

Adds the given integers together, returning the result.

#### Inputs:

* `$integers` is a list of integer values to be added.
* `$integer` is a single integer value to be added.

#### Outputs:

* `$integer` is the total sum of the given integers.

---

### tundra.integer:average

Returns the average value of the given list of integers.

#### Inputs:

* `$integers` is a list of integers.

#### Outputs:

* `$integer` is the average value of the given integers.

---

### tundra.integer:decrement

Decrements the given integer by one. Returns minus one if
no integer is specified, so that this service can be used
in a loop to both initialize and decrement a counter variable.

#### Inputs:

* `$integer` is the integer to be decremented by one.

#### Outputs:

* `$integer` is the given integer minus one, or minus one,
  if no input integer was specified.

---

### tundra.integer:divide

Divides the given integer x by y, returning the result.

#### Inputs:

* `$integer.x` is the numerator to be divided by `$integer.y`.
* `$integer.y` is the denominator to divide `$integer.x` by.

#### Outputs:

* `$integer` is the result of dividing `$integer.x` by `$integer.y`.

---

### tundra.integer:emit

Returns an string representation of the given integer object.

#### Inputs:

* `$object` is either a `java.math.BigDecimal`, `java.math.BigInteger`,
  `java.lang.Double`, `java.lang.Float`, `java.lang.Integer`, or
  `java.lang.Long` object.
* `$radix` is an optional [radix] used when encoding the integer as a
  string. Defaults to 10 (decimal), if not specified.

#### Outputs:

* `$string` is the resulting string representation of the integer.

---

### tundra.integer:increment

Increments the given integer by one. Returns one if no integer
was specified, so that this service can be used in a loop to
both initialize and increment a counter variable.

#### Inputs:

* `$integer` is the integer to be incremented by one.

#### Outputs:

* `$integer` is the given integer plus one, or one if no
  input integer was specified.

---

### tundra.integer:maximum

Returns the maximum value in the given list of integers.

#### Inputs:

* `$integers` is a list of integers.

#### Outputs:

* `$integer` is the integer in the given list with the
  largest value.

---

### tundra.integer:minimum

Returns the minimum value in the given list of integers.

#### Inputs:

* `$integers` is a list of integers.

#### Outputs:

* `$integer` is the integer in the given list with the
  smallest value.

---

### tundra.integer:multiply

Multiplies the given integers together, returning the result.

#### Inputs:

* `$integers` is a list of integer values to be multiplied.
* `$integer` is a single integer value to be multiplied.

#### Outputs:

* `$integer` is the result of multiplying all given integer
  values together.

---

### tundra.integer:negate

Returns the negative value of the given integer (-x).

#### Inputs:

* `$integer` is an integer to be negated.

#### Outputs:

* `$integer` is the given integer multiplied by minus
  one.

---

### tundra.integer:parse

Returns an integer object by parsing the given string.

#### Inputs:

* `$string` is a string of digits optionally preceded by a plus or minus
  sign to be parsed.
* `$class` is an optional Java class name that determines the type of
  object returned, a choice of either `java.math.BigInteger`,
  `java.math.BigDecimal`, `java.math.Double`, `java.math.Float`,
  `java.lang.Integer`, or `java.lang.Long`. Defaults to `java.math.BigInteger`,
  if not specified.
* `$radix` is an optional [radix] used to interpret the digits in the
  given `$string`. Defaults to 10 (decimal), if not specified.

#### Outputs:

* `$object` is the resulting Java object representing the parsed integer.

---

### tundra.integer:power

Raises the given integer to the power of the given exponent (i^e).

#### Inputs:

* `$integer` is an integer value.
* `$exponent` is the exponent to raise the integer by.

#### Outputs:

* `$integer` is the result of raising the given integer by
  the given exponent (i^e).

---

### tundra.integer:remainder

Returns the remainder from dividing the given integer x by y.

#### Inputs:

* `$integer.x` is the numerator to be divided by `$integer.y`.
* `$integer.y` is the denominator to divide `$integer.x` by.

#### Outputs:

* `$integer` is the remainder from dividing `$integer.x` by `$integer.y`.

---

### tundra.integer:subtract

Subtracts the given integer y from x, returning the result.

#### Inputs:

* `$integer.x` is an integer value to subtract `$integer.y` from.
* `$integer.y` is an integer value to subtract from `$integer.x`.

#### Outputs:

* `$integer` is the result of subtracting `$integer.y` from `$integer.x`.

---

### tundra.integer:validate

Returns true if the given string can be parsed as an integer.

#### Inputs:

* `$integer` is a string to be validated as an integer.
* `$raise?` is an optional boolean indicating whether an exception
  should be thrown if the given integer is invalid. Defaults to
  false.

#### Outputs:

* `$valid?` is a boolean indicating if the given string is a valid
  integer.

---

### tundra.json:emit

Serializes an IData document as a [JSON] formatted string, byte array, or
input stream.

#### Inputs:

* `$document` is the IData document to be serialized as a [JSON] string,
  byte array, or input stream.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to [UTF-8].
* `$mode` is an optional choice of {stream, bytes, string} which
  specifies the type of object `$content` is returned as. Defaults to
  stream.

#### Outputs:

* `$content` is the resulting serialization of `$document` as [JSON]
  content.

---

### tundra.json.mime.type:check

Returns true if the given MIME media type is recognized as a [JSON] media
type.

#### Inputs:

* `$content.type` is the MIME media type to be checked.

#### Outputs:

* `$json?` is a boolean which when true indicates that the given
  `$content.type` is a recognized [JSON] media type.

---

### tundra.json:parse

Parses [JSON] content specified as a string, byte array, or input stream
into an IData document.

#### Inputs:

* `$content` is a string, byte array, or input stream containing [JSON]
  content to be parsed.
* `$encoding` is an optional character set to use when $content is
  provided as a byte array or input stream to decode the contained text
  data. Defaults to [UTF-8].

#### Outputs:

* `$document` is the resulting IData document representing the parsed
  `$content`.

---

### tundra.list.content:emit

Emits (serializes) each item in the given IData[] document list to a
string, byte array, or input stream, resulting in a list of serialized
content.

Emitter implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:emit`
* Flat File: `WmFlatFile/pub.flatFile:convertToString`
* [JSON]: `Tundra/tundra.json:emit`
* Pipe separated values: `Tundra/tundra.csv:emit`
* [TSV]: `Tundra/tundra.csv:emit`
* [XML]: `WmPublic/pub.xml:documentToXMLString`
* [YAML]: `Tundra/tundra.yaml:emit`

#### Inputs:

* `$documents` is a list of IData documents to be serialized as a string,
  byte array, or input stream.
* `$content.types` is a list of MIME media types with the same number of
  items as `$documents`, where `$content.types[n]` describes the format
  of the resulting serialized `$document[n]`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$documents` contains unlike formats (for
  example, a mixture of [XML] and [JSON] MIME types).
* `$content.type` is the MIME media type that describes the format of all
  items in the resulting list of serialized content:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$documents` contains like formats (for
  example, when all items adhere to the exact same [XML] MIME type).
* `$namespaces` is a list of namespace prefixes and the URIs they map to,
  used when emitting [XML] content with elements in one or more
  namespaces. Use this input argument when `$documents` contains unlike
  formats (for example, a mixture of [XML] formats that use different
  namespaces).
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when emitting [XML] content with elements in one or more
  namespaces. Use this input argument when `$documents` contains like
  formats (for example, when all items adhere to the exact same [XML]
  MIME type using the same namespaces).
* `$schemas` is an optional input list with the same number of items as
  `$documents`, where `$schemas[n]` is used to serialize `$contents[n]`
  as [XML] or Flat File, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Defaults to serializing `$documents` as [XML], if neither
  `$content.types`, `$content.type`, `$schemas` nor `$schema` are
  specified.

  Use this input argument when `$documents` contains unlike formats (for
  example, a mixture of Flat File and [XML] formats).
* `$schema` is an optional input which determines whether to serialize
  all items in `$documents` as [XML], [JSON], Flat File, and can have the
  following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the document reference
    that defines the [XML] format.

  Defaults to serializing `$documents` as [XML], if neither `$schemas` or
  `$schema` are specified.

  Use this input argument when `$documents` contains like formats (for
  example, when all items adhere to the exact same [XML] schema).
* `$encoding` is an optional character set to use when the `$mode`
  selected is bytes or stream. Defaults to [UTF-8].
* `$mode` is an optional choice of stream, bytes, or string which
  determines the type of object the documents are serialized to.

#### Outputs:

* `$contents` is a list of strings, byte arrays, or input streams,
  depending on the `$mode` selected, where each item is the serialized
  version of the like-indexed document. In other words, `$contents[n]`
  is the serialized version of `$documents[n]`.

---

### tundra.list.content:join

Many-to-one conversion of content in one format to another format.

Calls the given joining service, passing the parsed list of contents as an
input, and emitting the joined content as output. The joining service must
accept an IData[] document list, and return a single IData document.

Parser implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:parse`
* Flat File: `WmFlatFile/pub.flatFile:convertToValues`
* [JSON]: `Tundra/tundra.json:parse`
* Pipe separated values: `Tundra/tundra.csv:parse`
* [TSV]: `Tundra/tundra.csv:parse`
* [XML]: `WmPublic/pub.xml:xmlStringToXMLNode`, `pub.xml:xmlNodeToDocument`
* [YAML]: `Tundra/tundra.yaml:parse`

Emitter implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:emit`
* Flat File: `WmFlatFile/pub.flatFile:convertToString`
* [JSON]: `Tundra/tundra.json:emit`
* Pipe separated values: `Tundra/tundra.csv:emit`
* [TSV]: `Tundra/tundra.csv:emit`
* [XML]: `WmPublic/pub.xml:documentToXMLString`
* [YAML]: `Tundra/tundra.yaml:emit`

#### Inputs:

* `$contents` is a list of strings, byte arrays, or input streams
  containing content (structured/parseable data) to be joined or
  aggregated together into one item of content.
* `$service` is the fully-qualified joining service name that is called
  to join or aggregate the parsed contents. This service must accept an
  IData[] document list, and return a single IData document.
* `$pipeline` is an optional IData document for specifying arbitrary
  input arguments to the invocation of $service.
* `$content.types.input` is a list of MIME media types with the same
  number of items as `$contents`, where `$content.types[n]` describes
  the format of the `$contents[n]`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$contents` contains unlike formats (for
  example, a mixture of [XML] and [JSON] MIME types).
* `$content.type.input` is the MIME media type that describes the format
  of all items in `$contents`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$contents` contains like formats (for
  example, when all items adhere to the exact same [XML] MIME type).
* `$content.type.output` is the MIME media type that describes the
  format of the returned `$content`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$contents` contains like formats (for
  example, when all items adhere to the exact same [XML] MIME type).
* `$namespaces.input` is a list of namespace prefixes and the URIs they map to,
  used when parsing [XML] content with elements in one or more
  namespaces. Use this input argument when `$contents` contains unlike
  formats (for example, a mixture of [XML] formats that use different
  namespaces).
* `$namespace.input` is a list of namespace prefixes and the URIs they map to,
  used when parsing [XML] content with elements in one or more
  namespaces. Use this input argument when `$contents` contains like
  formats (for example, when all items adhere to the exact same [XML]
  MIME type using the same namespaces).
* `$namespace.output` is a list of namespace prefixes and the URIs they map to,
  used when emitting [XML] content with elements in one or more
  namespaces.
* `$schemas.input` is an optional input list with the same number of
  items as `$contents`, where `$schemas[n]` is used to parse
  `$contents[n]` as [XML] or Flat File, and can have the following
  values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Use this input argument when `$contents` contains unlike formats (for
  example, a mixture of flat file and xml formats).

  Defaults to parsing `$contents` as [XML], if neither
  `$content.types.input`, `$content.type.input`, `$schemas.input` nor
  `$schema.input` are specified.
* `$schema.input` is an optional input which determines whether to parse
  all items in `$contents` as [XML] or Flat File, and can have the
  following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Use this input argument when `$contents` contains like formats (for
  example, when all items adhere to the exact same XML schema).

  Defaults to parsing `$contents` as [XML], if neither
  `$content.types.input`, `$content.type.input`, `$schemas.input` nor
  `$schema.input` are specified.
* `$schema.output` is an optional input which determines whether to
  serialize the document returned by `$service` as [XML] or Flat File,
  and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to serializing as [XML].
* `$service.input` is an optional name to use for the parsed IData[]
  document list for the input pipeline of the `$service` invocation.
  Defaults to `$documents`.
* `$service.output` is an optional name to use for the output IData
  parameter returned by the `$service` invocation. Defaults to
  `$document`.
* `$encoding.input` is an optional character set to use when the
  `$contents` is provided as a list of input streams or byte arrays.
  Defaults to [UTF-8].
* `$encoding.output` is an optional character set to use when the
  `$mode.output` selected is bytes or stream. Defaults to [UTF-8].
* `$mode.output` is an optional choice of stream, bytes, or
  string, which determines the type of `$content` object returned.
  Defaults to stream.

#### Outputs:

* `$content` is a string, byte array, or input stream, depending on
  the `$mode.output` selected, of the serialized IData document
  returned by ``$service.

---

### tundra.list.content:parse

Parses a list of content (specified as a list of strings, bytes, or input
streams) into an IData[] document list.

Parser implementions for the supported content types are as follows:
* [CSV]: `Tundra/tundra.csv:parse`
* Flat File: `WmFlatFile/pub.flatFile:convertToValues`
* [JSON]: `Tundra/tundra.json:parse`
* Pipe separated values: `Tundra/tundra.csv:parse`
* [TSV]: `Tundra/tundra.csv:parse`
* [XML]: `WmPublic/pub.xml:xmlStringToXMLNode`, `pub.xml:xmlNodeToDocument`
* [YAML]: `Tundra/tundra.yaml:parse`

#### Inputs:

* `$contents` is a list of strings, byte arrays, or input streams
  containing content (structured/parseable data) to be parsed.
* `$content.types` is a list of MIME media types with the same number of
  items as `$contents`, where `$content.types[n]` describes the format
  of the resulting parsed `$content[n]`:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$contents` contains unlike formats (for
  example, a mixture of [XML] and [JSON] MIME types).
* `$content.type` is the MIME media type that describes the format of all
  items in the resulting list of parsed content:
  * [CSV]: specify a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix.
  * Flat File: optionally specify any MIME media type.
  * [JSON]: specify a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix.
  * Pipe separated values: specify a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv"
    suffix.
  * [TSV]: specify a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix.
  * [YAML]: specify a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix.
  * [XML]: optionally specify a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a "+xml"
    suffix.

  Use this input argument when `$contents` contains like formats (for
  example, when all items adhere to the exact same [XML] MIME type).
* `$namespaces` is a list of namespace prefixes and the URIs they map to,
  used when parsing [XML] content with elements in one or more
  namespaces. Use this input argument when `$contents` contains unlike
  formats (for example, a mixture of [XML] formats that use different
  namespaces).
* `$namespace` is a list of namespace prefixes and the URIs they map to,
  used when parsing [XML] content with elements in one or more
  namespaces. Use this input argument when `$contents` contains like
  formats (for example, when all items adhere to the exact same [XML]
  MIME type using the same namespaces).
* `$schemas` is an optional input list with the same number of items as
  `$documents`, where `$schemas[n]` is used to parse `$contents[n]` as
  [XML] or Flat File, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Defaults to parsing `$contents` as [XML], if neither `$content.types`,
  `$content.type`, `$schemas` nor `$schema` are specified.

  Use this input argument when `$contents` contains unlike formats (for
  example, a mixture of Flat File and [XML] formats).
* `$schema` is an optional input used to parse all items in `$contents`
  as [XML] or Flat File, and can have the following values:
  * [CSV]: do not specify.
  * Flat File: specify the fully-qualified name of the Integration Server
    Flat File Schema element that defines the Flat File format.
  * [JSON]: do not specify.
  * Pipe separated values: do not specify.
  * [TSV]: do not specify.
  * [YAML]: do not specify.
  * [XML]: specify the fully-qualified name of the Integration Server
    document reference that defines the [XML] format.

  Use this input argument when `$contents` contains like formats (for
  example, when all items adhere to the exact same [XML] schema).

  Defaults to parsing `$contents` as [XML], if neither `$content.types`,
  `$content.type`, `$schemas` nor `$schema` are specified.
* `$encoding` is an optional character set to use when the `$contents` is
  provided as a list of input streams or byte arrays. Defaults to [UTF-8].

#### Outputs:

* `$documents` is an IData[] document list of the parsed `$contents`,
  where `$documents[n]` is the parsed version of `$contents[n]`.

### Datetime List

---

### tundra.list.datetime:format

Formats a list of datetime strings that conform to the input
pattern, according to the output pattern.

Supports a handful of well-known named datetime patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.
SimpleDateFormat] compatible patterns.

#### Inputs:

* `$list` is a list of datetime strings to be formatted.
* `$pattern.input` is an optional datetime pattern that `$list`
  conforms to, that will be used to parse the datetime strings.
  Defaults to an [ISO8601] XML datetime.
* `$patterns.input` is an optional list of datetime patterns that
  `$list` might conform to, useful when the exact pattern is not
  known. A parse is attempted for each pattern until the first
  successful parse, or until all patterns have been tried in which
  case an unparseable datetime exception will be thrown.
* `$pattern.output` is an optional datetime pattern that will be
  used to format the resulting `$list` strings. Defaults to an
  [ISO8601] XML datetime.
* `$timezone.input` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost
  time zone identifying the time zone the input `$list` strings
  will be parsed with. If specified, this time zone will override
  any zone specified in the `$list` strings themselves.
* `$timezone.output` is an optional [java.util.TimeZone] ID, or a
  `(+|-)HH:mm` time zone offset, or an XML duration string
  representing a time zone offset, or a raw millisecond time zone
  offset, or `Z` for UTC, or `local` for the default localhost
  time zone identifying the time zone the returned $list strings
  will be formatted with.

#### Outputs:

* `$list` is the resulting list of datetime strings formatted
  according to `$pattern.output` in the given `$timezone.output`.

---

### tundra.list.document:append

Appends a single item to the end of a list, such that appending an item
to a list containing n items results in a new list of n + 1 items.

#### Inputs:

* `$list` is a list to append an item to.
* `$item` is an item to be appended to the given list.

#### Outputs:

* `$list` is the resulting list with the given `$item` appended to the
  end.

---

### tundra.list.document:clear

Removes all elements from all items in the given IData document list,
except for any keys specified in the preserve list.

#### Inputs:

* `$list` is an IData document list for which to remove the keys in
  each item.
* `$preserve` is list of keys which will not be removed from
  the given IData document list. Keys can be simple or fully
  qualified, such as `a/b/c[0]/d`.

#### Outputs:

* `$list` is the given IData document list with all keys removed
  from each item, except for those specified in `$preserve`.

---

### tundra.list.document:compact

Removes all null items from the given list, thereby shortening the
length of the list.

#### Inputs:

* `$list` is a list to be compacted.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be compacted. Defaults to false.

#### Outputs:

* `$list` is the given list with all null items removed.

---

### tundra.list.document:concatenate

Returns a new list containing all the items in the given lists.

#### Inputs:

* `$list.x` is the first list to be concatenated.
* `$list.y` is the second list to be concatenated.

#### Outputs:

* `$list` is a new list containing all the items from the given
  input lists.

---

### tundra.list.document:drop

Removes the item stored at a given index in the given list.

#### Inputs:

* `$list` is a list to remove the item from.
* `$index` is an integer identifying which item to be removed from
  the given list. List indexing is zero-based. Supports both
  forward and reverse indexing (where, for example, an index of
  -1 is the last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the given list with the item identified by the given
  index removed.

---

### tundra.list.document:each

Iterates through the given list, invoking the given service for each
item in the list, passing `$item`, `$index`, `$iteration` and `$length`
variables.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking `$service`.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of `$service`. Defaults to
  $item.

---

### tundra.list.document:equal

Returns true if the two given lists are equal.

#### Inputs:

* `$list.x` is a list to be compared with `$list.y`.
* `$list.y` is a list to be compared with `$list.x`.

#### Outputs:

* `$equal?` is a boolean indicating if `$list.x` equals
  `$list.y`.

---

### tundra.list.document:filter

Filters the given list to only include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to only include items
  where surname and firstname keys have specific values,
  use the following `$condition`:

      %$item/surname% == "Smith" and %$item/firstname% == "John"

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to only include the
  items where `$condition` evaluated to true.

---

### tundra.list.document:first

Returns the first item from the given list.

#### Inputs:

* `$list` is a list to fetch the first item from.

#### Outputs:

* `$item` is the first item in the given list.

---

### tundra.list.document:get

Returns the item stored at a given index in a list. A zero-
based index can be specified using the `$index` input, or
a one-based index can be specified using the `$iteration` input
(which is useful when using this service inside a flow loop).

#### Inputs:

* `$list` is a list to fetch an item from.
* `$index` is an optional zero-based index identifying
  the item to be fetched. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).
* `$iteration` is an optional one-based index identifying
  the item to be fetched.

#### Outputs:

* `$item` is the item stored at the given index in the given
  list.

---

### tundra.list.document:group

Groups the given IData document list items by the given keys. This
service can be used to process an IData document list in a way
similar to a [SQL GROUP BY] clause.

#### Inputs:

* `$list` is an IData document list whose items are to be grouped.
* `$keys` is a String list containg the keys used to group like items
  in `$list` together, and can be simple or fully qualified, such
  as `a/b/c[0]/d`.

#### Outputs:

* `$list.grouped` is an IData document list containing one item per
  each unique set of grouping key value tuples associated with
  `$keys` from `$list`:
  * `group` is an IData document containing the keys and values
    that defined this group.
  * `items` is an IData document list containing all the items from
    `$list` whose keys and values are equal to key value tuples in
    the associated `group`.

---

### tundra.list.document:grow

Increases the size of the given list by the given count of items, padded
with the given item (or null if not specified).

#### Inputs:

* `$list` is the IData[] list to be grown.
* `$item` is an optional IData used to pad the newly grown section of the
  list with.
* `$count` is the number of new items to add to the list.

#### Outputs:

* `$list` is the IData[] list grown by the desired `$count` of items,
  with the original items preserved and the new items padded with `$item`
  (or null if not specified).

---

### tundra.list.document:include

Returns true if the given item is found in the given list.

#### Inputs:

* `$list` is a list to check whether the given `$item` exists
  in.
* `$item` is the item to be checked against the given `$list`.

#### Outputs:

* `$include?` is a boolean indicating the the given `$item`
  exists as an item in the given `$list`.

---

### tundra.list.document:insert

Returns a new list with the given item inserted at the desired
index in the given list.

#### Inputs:

* `$list` is a list to check whether the given `$item` exists
  in.
* `$item` is the item to be checked against the given `$list`.
* `$index` is the index at which to insert the item. List
  indexing is zero-based. Supports both forward and
  reverse indexing (where, for example, an index of -1
  is the last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the resulting list with the item inserted at the
  desired index.

---

### tundra.list.document:join

Many-to-one conversion of an IData document list to an IData document.

Calls the given joining service, passing the IData document list
as an input, and returning the resulting IData document as output.
The joining service must accept an IData document list, and
return a single IData document.

#### Inputs:

* `$documents` is an IData document list to be joined or
  aggregated together into one IData document.
* `$service` is the fully-qualified joining service name that is
  called to join or aggregate the IData document list. This
  service must accept an IData[] array, and return a single
  IData object.
* `$pipeline` is an optional IData document for specifying
  arbitrary input arguments to the invocation of `$service`.
* `$service.input` is an optional name to use for the IData
  document list for the input pipeline of the `$service` invocation.
  Defaults to $documents.
* `$service.output` is an optional name to use for the output IData
  parameter returned by the `$service` invocation. Defaults to
  $document.

#### Outputs:

* `$document` is the resulting IData document returned by `$service`.

---

### tundra.list.document.key:lowercase

Converts all keys in each item in the given IData document list to
lower case.

#### Inputs:

* `$list` is an IData document list whose item's keys are to be
  converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their keys converted to lower case. Defaults to
  false.

#### Outputs:

* `$list` is the given IData document list with all item's keys
  converted to lower case.

---

### tundra.list.document.key:replace

Replaces all occurrences of the given [regular expression pattern]
in each item's keys in the given IData document list with the
replacement string.

#### Inputs:

* `$list` is an IData document list to have all occurrences of the
  given [regular expression pattern] in each item's keys replaced.
* `$pattern` is the [regular expression pattern] to match against
  each key.
* `$replacement` is the replacement string to be substituted in
  each key wherever the given pattern is found.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have occurrences of the pattern in their string values
  replaced. Defaults to false.

#### Outputs:

* `$list` is the given IData document list with all occurrences of
  the given [regular expression pattern] in each item's keys replaced
  with `$replacement`.

---

### tundra.list.document.key:trim

Removes leading and trailing whitespace from all item's keys
in the given IData document list.

#### Inputs:

* `$list` is an IData document list whose item's keys are to
  be trimmed of leading and trailing whitespace characters.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their keys trimmed. Defaults to false.

#### Outputs:

* `$list` is the given IData document list with all item's
  keys trimmed of leading and trailing whitespace characters
  removed.

---

### tundra.list.document.key:uppercase

Converts all keys in each item in the given IData document list to
upper case.

#### Inputs:

* `$list` is an IData document list whose item's keys are to be
  converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their keys converted to upper case. Defaults to
  false.

#### Outputs:

* `$list` is the given IData document list with all item's keys
  converted to upper case.

---

### tundra.list.document:keys

Returns the union set of all top-level keys present in each IData
item in the given IData[] document list that match the given regular
expression pattern if specified, or all top-level keys if no pattern
is specified.

#### Inputs:

* `$list` is an IData[] document list from which all top-level
  keys are to be fetched.
* `$pattern` is an optional [regular expression pattern] that
  is used to filter the list of keys returned. If not specified,
  all keys are returned.

#### Outputs:

* `$keys` is the union set of all top-level keys in the given IData[]
  document list that match the given regular expression `$pattern` if
  specified, or all top-level keys if no `$pattern` was specified.

---

### tundra.list.document:last

Returns the last item from the given list.

#### Inputs:

* `$list` is a list to fetch the last item from.

#### Outputs:

* `$item` is the last item in the given list.

---

### tundra.list.document:length

Returns the number of items in the given list.

#### Inputs:

* `$list` is a list to count the number of items in.

#### Outputs:

* `$length` is the number of items in the given list.

---

### tundra.list.document:map

Returns a new list created by invoking the given service for each
item in the input list, passing `$item`, `$index`, `$iteration` and
`$length` variables, and collecting the values returned by the service
to form the new list.

This is an implementation of a higher-order [map function] for
document lists.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking `$service`.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of `$service`. Defaults to
  $item.
* `$item.output` is an optional variable name used when extracting
  the resulting item from the invocation of `$service`. Defaults to
  $item.

#### Outputs:

* `$list` is the newly constructed list containing the returned
  items from invoking `$service` for each input list item.

---

### tundra.list.document:pivot

Returns a given `IData[]` document list pivoted on a given list of
keys.

For example, given an `IData[]` document list that includes the
following two items (represented in [JSON] form):

    $list = [
      { "id": "1", "name": "Bob",  "phone": "1234 5678" },
      { "id": "2", "name": "Jane", "phone": "2345 6789" }
    ]

A pivot on the key `name` would return the following `IData` document,
where each list item is now associated with the value associated
with the pivot key:

    $document = {
      "Bob":  { "id": "1", "name": "Bob",  "phone": "1234 5678" },
      "Jane": { "id": "2", "name": "Jane", "phone": "2345 6789" }
    }

A pivot on the key `phone` would return the following `IData` document:

    $document = {
      "1234 5678": { "id": "1", "name": "Bob",  "phone": "1234 5678" },
      "2345 6789": { "id": "2", "name": "Jane", "phone": "2345 6789" }
    }

A pivot on both the keys `id` and `name` would return the following
`IData` document (note that the pivot keys are nested):

    $document = {
      "1": {
        "Bob":  { "id":"1", "name":"Bob",  "phone":"1234 5678" }
      },
      "2": {
        "Jane": { "id":"2", "name":"Jane", "phone":"2345 6789" }
      }
    }

Alternatively, a pivot on both the keys `id` and `name` with a delimiter
of `-` would construct a compound key using the values associated with
each key from each item joined with the given delimiter, as follows:

    $document = {
      "1-Bob":  { "id":"1", "name":"Bob",  "phone":"1234 5678" },
      "2-Jane": { "id":"2", "name":"Jane", "phone":"2345 6789" }
    }

Although the difference between the `IData[]` document list and `IData`
document representations appear subtle, a pivot of a `IData[]`
document list on a primary key returns an `IData` document that can be
efficiently accessed by key, rather than having to iterate over the
list to find a specific item by key.

#### Inputs:

* `$list` is the `IData[]` document list to be pivoted.
* `$keys` is the list of keys to pivot on, with the following
  caveats:
  * If a key doesn't exist in an item, that item is not included
    in the resulting pivot.
  * If a key is associated with a null value in an item, that item
    is not included in the resulting pivot.
  * If a key is associated with the same value in multiple items,
    only the first item is included in the resulting pivot.
* `$delimiter` is an optional character to use for separating the
  values that form a compound key, when multiple pivot keys are
  specified. If not specified, the pivot keys will be nested in
  the returned `IData` document. The delimiter `/` is reserved,
  and will also result in the pivot keys being nested.

#### Outputs:

* `$document` is the `IData` document pivoted representation of the
  given `$list`.

---

### tundra.list.document:prepend

Prepends a single item to the front of a list, such that prepending
an item to a list containing n items results in a new list of n + 1
items.

#### Inputs:

* `$list` is a list to be prepended to.
* `$item` is the item to prepend to the given list.

#### Outputs:

* `$list` is the resulting list with the given `$item` prepended to the
  start.

---

### tundra.list.document:put

Sets the value of the item at the given index in the given list.

#### Inputs:

* `$list` is a list in which to set the given value.
* `$item` is the item to be set in the given list.
* `$index` is an optional zero-based index identifying
  the item to be set. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the resulting list with the item at the given
  index set to the given value.

---

### tundra.list.document:reject

Filters the given list to not include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to not include items
  where surname and firstname keys have specific values,
  use the following `$condition`:

      %$item/surname% == "Smith" and %$item/firstname% == "John"

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to not include the
  items where `$condition` evaluated to true.

---

### tundra.list.document:resize

Resizes the given list to the given length, truncated from the end when the
length is decreased, and padded with the given item (or null if not
specified) when the length is increased.

#### Inputs:

* `$list` is the IData[] list to be grown.
* `$item` is an optional IData used to pad the list if increasing the
  size.
* `$length` is the desired new length of the list if specified as a
  positive integer, or the relative number of items to truncate from the
  list if specified as a negative integer.

#### Outputs:

* `$list` is the IData[] list resized to the desired `$length`; if the
  new length is less than the original length, the list is truncated from
  the end; if the new length is greater than the original length, the
  list is padded with `$item` (or null if not specified).

---

### tundra.list.document:reverse

Returns a new list with all items from the given list in
reverse order.

#### Inputs:

* `$list` is the list to be reversed.

#### Outputs:

* `$list` is the given list with item ordering reversed.

---

### tundra.list.document:shrink

Decreases the size of the given list by the given count, truncating items
from the end.

#### Inputs:

* `$list` is the IData[] list to be shrunk.
* `$count` is the number of items to truncate from the end of the list.

#### Outputs:

* `$list` is the IData[] list shrunk from the end of the list by the
  desired item count by truncating items from the end of the list. If
  the list is smaller than the count, an empty list is returned.

---

### tundra.list.document:slice

Returns a new list which is a subset of the items in the
given list.

#### Inputs:

* `$list` is the list to be sliced.
* `$index` is the zero-based start index from which to
  take the slice.
* `$length` is the number of items to include in the
  slice.

#### Outputs:

* `$list` is the desired subset or slice of the given list.

---

### tundra.list.document:sort

Returns a new `IData[]` document list sorted according to the [natural
ordering] of the values associated with the given keys in each list
item.

#### Inputs:

* `$list` is the `IData[]` document list to be sorted.
* `$criteria` is an `IData[]` document list containing list of sort
  criteria in order of precedence, where the first item in the
  list is the most significant criteria and the last item is the
  least significant criteria:
  * `key` is the fully-qualified key identifying the values in `$list`
    on which to sort.
  * `type` is an optional choice that defines type of comparison
    performed between the values associated with the `key`:
    * `string` will compare the values as strings.
    * `integer` will compare the values as integers.
    * `decimal` will compare the values as decimal numbers.
    * `datetime` will compare the values as datetimes.
    * `duration` will compare the values as durations of time.
  * `pattern` is an optional pattern string used when the type of
    comparison is either datetime (in which case a pattern
    compatible with `Tundra/tundra.datetime:format` must be
    specified), or duration (in which case a pattern compatible
    with `Tundra/tundra.duration:format` must be specified).
  * `descending?` is an optional boolean indicating if the values
    associated with the `key` are to be sorted in descending
    (largest to smallest) order. Defaults to false, if not
    specified.

#### Outputs:

* `$list` is the sorted `IData[]` document list.

---

### tundra.list.document:squeeze

Trims all leading and trailing whitespace from all string values,
then converts empty strings, empty IData documents, and empty lists
to nulls, then compacts the IData[] document list by removing all
null values.

#### Inputs:

* `$list` is an IData[] document list to be squeezed.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData[] document lists should also
  be squeezed. Defaults to false.

#### Outputs:

* `$list` is the resulting IData[] document list with all string
  values trimmed of leading and trailing whitespace characters, and
  all empty string values, empty IData documents, empty lists, and
  null values removed.

---

### tundra.list.document:substitute

Attempts variable substitution on each string value in the given IData
document list by replacing all occurrences of substrings matching "%key%"
with the associated (optionally scoped) value.

Optionally replaces null or non-existent values with the given default
value.

#### Inputs:

* `$list` is an IData document list to perform variable substitution on.
* `$pipeline` is an optional scope used to resolve key references. If
  not specified, keys are resolved against the pipeline itself.
* `$default` is an optional default value to substitute in place of keys
  that resolve to null or missing values. If not specified, no
  substitution will be made for keys that resolve to null or missing
  values.

#### Outputs:

* `$list` is the resulting IData document list with all variable
  substitution patterns in all item's values, such as "%key%", replaced
  with the value of the key (resolved against either `$pipeline`, if
  specified, or the pipeline itself).

---

### tundra.list.document:translate

One-to-one conversion of one IData document list to another IData document
list.

Calls the given translation service, passing the each IData document item
in the list as an input, and building a new IData document list from the
translated documents returned by the translation service as output.

#### Inputs:

* `$list` is an IData document list containing items to be translated.
* `$service` is the fully-qualified name of the translation service,
  which accepts a single IData document and returns a single IData
  document, called to translate the given `$document`.
* `$pipeline` is an optional IData document for providing arbitrary
  variables to the invocations of `$service`.
* `$service.input` is an optional variable name to use in the input
  pipeline of the call to `$service` for the given IData document.
  Defaults to $document.
* `$service.output` is an optional variable name used to extract the
  output IData document from the output pipeline of the call to
  `$service`. Defaults to `$translation`.

#### Outputs:

* `$translations` is the resulting IData document list containing the
  translated documents.

---

### tundra.list.document.value:lowercase

Converts all item's string values in the given IData document
list to lower case.

#### Inputs:

* `$list` is an IData document list whose item's string values
  are to be converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their string values converted to lower case. Defaults
  to false.

#### Outputs:

* `$list` is the given IData document list with all item's string
  values converted to lower case.

---

### tundra.list.document.value:replace

Replaces all occurrences of the given [regular expression pattern]
in each item's string values in the given IData document list with
the replacement string.

#### Inputs:

* `$list` is an IData document list to have all occurrences of the
  given [regular expression pattern] in each item's string values
  replaced.
* `$pattern` is the [regular expression pattern] to match against
  each string value.
* `$replacement` is the replacement string to be substituted in
  each string value wherever the given pattern is found.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have occurrences of the pattern in their string values
  replaced. Defaults to false.

#### Outputs:

* `$list` is the given IData document list with all occurrences of
  the given [regular expression pattern] in each item's string
  values replaced with `$replacement`.

---

### tundra.list.document.value:trim

Removes leading and trailing whitespace from all item's string
values in the given IData document list.

#### Inputs:

* `$list` is an IData document list whose item's string values
  are to be trimmed of leading and trailing whitespace
  characters.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their string values trimmed. Defaults to false.

#### Outputs:

* `$list` is the given IData document list with all item's string
  values trimmed of leading and trailing whitespace characters.

---

### tundra.list.document.value:uppercase

Converts all item's string values in the given IData document
list to upper case.

#### Inputs:

* `$list` is an IData document list whose item's string values
  are to be converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.
* `$recurse?` is an optional boolean indicating if embedded
  IData documents and IData document lists should also
  have their string values converted to upper case. Defaults to
  false.

#### Outputs:

* `$list` is the given IData document list with all string values
  converted to upper case.

---

### tundra.list.document:values

Returns a list of values associated with the given key from the
respective item in the given `IData[]` document list.

#### Inputs:

* `$list` is an `IData[]` document list.
* `$key` is the key for which the associated value from each item in
  the given `$list` will be returned, and can be simple or fully
  qualified, such as `a/b/c[0]/d`.
* `$default.object` is an optional value to be returned when either
  the given `$key` does not exist in an item or its associated value
  is null.
* `$default.string` is an optional string value, provided for
  convenience when hard-coding a default value, to be returned
  when either the given `$key` does not exist in an item or its
  associated value is null.

#### Outputs:

* `$values` is the list of values associated with `$key` from the
  respective item in `$list`, or the given `$default.object` or
  `$default.string` if specified and the associated value for `$key`
  either did not exist or was null in the respective item.

---

### tundra.list.duration:format

Formats a list of duration strings according to the desired
pattern.

A start instant may be required when formatting fields with
indeterminate values, such as converting months to days
(because the number of days in a month varies).

#### Inputs:

* `$list` is a list of duration strings to be reformatted.
* `$pattern.input` is the duration pattern the given `$list` of
  duration strings adhere to.
* `$pattern.output` is the desired duration pattern the `$list` of
  duration strings will be reformatted according to.
* `$datetime` is an optional datetime string used as a start
  instant for resolving indeterminate durations (such as the
  number of days in a month).
* `$datetime.pattern` is an optional datetime pattern that `$datetime`
  conforms to, that will be used to parse the datetime string.
  Defaults to an [ISO8601] XML datetime.

#### Outputs:

* `$list` is the given list of duration strings formatted according
  to `$pattern.output`.

---

### tundra.list.duration:sum

Returns the sum of all the given durations, returning
(x1 + x2 + ... + xn).

#### Inputs:

* `$list` is a list of [ISO8601] XML duration strings to be added
  together.
* `$pattern.input` is an optional pattern describing the type of
  duration strings in `$list`. Defaults to an [ISO8601] XML string.
* `$pattern.output` is an optional desired pattern used to format
  the returned `$duration` string. Defaults to an [ISO8601] XML
  string.

#### Outputs:

* `$duration` is the sum of the duration strings in the given list
  in [ISO8601] XML format.

---

### tundra.list.html:decode

[HTML] decodes the given string list.

[HTML entities], such as `&lt;` and `&gt;`, are decoded to the
appropriate character representation, such as `<` and `>`.

#### Inputs:

* `$list` is an optional string list to be [HTML] decoded.

#### Outputs:

* `$list` is the given string list with [HTML entities] decoded
  in each item in the list.

---

### tundra.list.html:emit

Serializes an `IData[]` document list as an [HTML] fragment suitable
for embedding in an [HTML] page, formatted as a string, byte array,
or input stream.

Child elements that are `IData` documents, `IData[]` document lists,
`Object[]` object lists, or `Object[][]` object tables are recursively
serialized to clean [HTML] table elements.

#### Inputs:

* `$list` is the `IData[]` document list to be serialized as an [HTML]
  string, byte array, or input stream.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to
  [UTF-8].
* `$mode` is an optional choice of stream, bytes, or string, which
  specifies the type of object `$content` is returned as. Defaults
  to stream.

#### Outputs:

* `$content` is the resulting serialization of `$list` as [HTML]
  content.

---

### tundra.list.html:encode

[HTML] encodes the given string list.

Reserved characters in [HTML], such as `<` and `>`, are encoded to the
appropriate [HTML entity], such as `&lt;` and `&gt;`, to ensure the [HTML]
is rendered correctly by web browsers and other [HTML] rendering
software.

#### Inputs:

* `$list` is an optional string list to be [HTML] encoded.

#### Outputs:

* `$list` is the given string list with special characters [HTML]
  encoded in each item in the list.

---

### tundra.list.object:append

Appends a single item to the end of a list, such that appending an item
to a list containing n items results in a new list of n + 1 items.

#### Inputs:

* `$list` is a list to append an item to.
* `$item` is an item to be appended to the given list.
* `$class` is an optional Java class name that the list and item
  to be appended are required to be instances of.

#### Outputs:

* `$list` is the resulting list with the given `$item` appended to the
  end.

---

### tundra.list.object:coalesce

Returns the first object in the given list that is not null, or if
all objects in the list are null, then the given item.

#### Inputs:

* `$list` is an optional list of objects.
* `$default` is an optional object, included for convenience when using
  this service in a map step and providing a default value.
* `$mode` determines what is returned when all arguments are null:
  * `missing`: `$item` is not returned when all arguments are null.
    This is the default, if `$mode` is not specified.
  * `null`: `$item` is returned as null when all arguments are null.

#### Outputs:

* `$item` is the first object in the given `$list` that is not null,
  or the given input `$item` if it is not null, or null if all
  other values were null and `$mode` is 'null'.

---

### tundra.list.object:compact

Removes all null items from the given list, thereby shortening the
length of the list.

#### Inputs:

* `$list` is a list to be compacted.

#### Outputs:

* `$list` is the given list with all null items removed.

---

### tundra.list.object:concatenate

Returns a new list containing all the items in the given lists.

#### Inputs:

* `$list.x` is the first list to be concatenated.
* `$list.y` is the second list to be concatenated.

#### Outputs:

* `$list` is a new list containing all the items from the given
  input lists.

---

### tundra.list.object:difference

Returns a list of only the items in `$list.x` that are not also
present in `$list.y`.

#### Inputs:

* `$list.x` is a list to be checked for differences against `$list.y`.
* `$list.y` is a list to be checked against `$list.x` for differnces.

#### Outputs:

* `$list` is a new list containing only the items in `$list.x` that
  are not also present in `$list.y`.

---

### tundra.list.object:drop

Removes the item stored at a given index in the given list.

#### Inputs:

* `$list` is a list to remove the item from.
* `$index` is an integer identifying which item to be removed from
  the given list. List indexing is zero-based. Supports both
  forward and reverse indexing (where, for example, an index of
  -1 is the last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the given list with the item identified by the given
  index removed.

---

### tundra.list.object:each

Iterates through the given list, invoking the given service for each
item in the list, passing `$item`, `$index`, `$iteration` and `$length`
variables.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking $service.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of $service. Defaults to
  $item.

---

### tundra.list.object:equal

Returns true if the two given lists are equal.

#### Inputs:

* `$list.x` is a list to be compared with `$list.y`.
* `$list.y` is a list to be compared with `$list.x`.

#### Outputs:

* `$equal?` is a boolean indicating if `$list.x` equals
  `$list.y`.

---

### tundra.list.object:filter

Filters the given list to only include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to only include items
  equal to 1 or 2, use the following `$condition`:

      %$item% == 1 or %$item% == 2

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to only include the
  items where `$condition` evaluated to true.

---

### tundra.list.object:first

Returns the first item from the given list.

#### Inputs:

* `$list` is a list to fetch the first item from.

#### Outputs:

* `$item` is the first item in the given list.

---

### tundra.list.object:get

Returns the item stored at a given index in a list. A zero-
based index can be specified using the `$index` input, or
a one-based index can be specified using the `$iteration` input
(which is useful when using this service inside a flow loop).

#### Inputs:

* `$list` is a list to fetch an item from.
* `$index` is an optional zero-based index identifying
  the item to be fetched. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).
* `$iteration` is an optional one-based index identifying
  the item to be fetched.

#### Outputs:

* `$item` is the item stored at the given index in the given
  list.

---

### tundra.list.object:grow

Increases the size of the given list by the given count of items, padded
with the given item (or null if not specified).

#### Inputs:

* `$list` is the Object[] list to be grown.
* `$item` is an optional Object used to pad the newly grown section of
  the list with.
* `$count` is the number of new items to add to the list.
* `$class` is an optional Java class name used to instantiate a new list
  if the provided `$list` is null.

#### Outputs:

* `$list` is the Object[] list grown by the desired `$count` of items,
  with the original items preserved and the new items padded with `$item`
  (or null if not specified).

---

### tundra.list.object:include

Returns true if the given item is found in the given list.

#### Inputs:

* `$list` is a list to check whether the given $item exists
  in.
* `$item` is the item to be checked against the given $list.

#### Outputs:

* `$include?` is a boolean indicating the the given `$item`
  exists as an item in the given `$list`.

---

### tundra.list.object:insert

Returns a new list with the given item inserted at the desired
index in the given list.

#### Inputs:

* `$list` is a list to check whether the given `$item` exists
  in.
* `$item` is the item to be checked against the given `$list`.
* `$index` is the index at which to insert the item. List
  indexing is zero-based. Supports both forward and
  reverse indexing (where, for example, an index of -1
  is the last item in the list, and an index of -2 is the
  second last item in the list).
* `$class` is an optional Java class name that the list and
  item to be inserted are required to be instances of.

#### Outputs:

* `$list` is the resulting list with the item inserted at the
  desired index.

---

### tundra.list.object:instance

Returns true if the given list is an instance of the given class.

#### Inputs:

* `$list` is a list to check whether it is an instance of
  the given Java class.
* `$class` is the [Java array class name] used to check the list
  against.

#### Outputs:

* `$instance?` is a boolean indicating if the given list is an
  instance of the given class.

---

### tundra.list.object:intersection

Returns a list of only the items in `$list.x` that are also present in
`$list.y`.

#### Inputs:

* `$list.x` is a list to be intersected with `$list.y`.
* `$list.y` is a list to be intersected with `$list.x`.

#### Outputs:

* `$list` is a new list containing only the items that are present
  in both input lists (the [set intersection]).

---

### tundra.list.object:join

Returns a string created by converting each list item to a string, and
concatenating the resulting strings together separated by the given
`$separator`.

#### Inputs:

* `$list` is a list to be converted to a string.
* `$separator` is an optional string used to separate each list item
  in the resulting string. Defaults to an empty string.

#### Outputs:

* `$result` is a string containing each item in the given list, converted
  to a string and separated by the given `$separator`.

---

### tundra.list.object:last

Returns the last item from the given list.

#### Inputs:

* `$list` is a list to fetch the last item from.

#### Outputs:

* `$item` is the last item in the given list.

---

### tundra.list.object:length

Returns the number of items in the given list.

#### Inputs:

* `$list` is a list to count the number of items in.

#### Outputs:

* `$length` is the number of items in the given list.

---

### tundra.list.object:map

Returns a new list created by invoking the given service for each
item in the input list, passing `$item`, `$index`, `$iteration` and
`$length` variables, and collecting the values returned by the service
to form the new list.

This is an implementation of a higher-order [map function] for
object lists.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking $service.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of `$service`. Defaults to
  $item.
* `$item.output` is an optional variable name used when extracting
  the resulting item from the invocation of `$service`. Defaults to
  $item.

#### Outputs:

* `$list` is the newly constructed list containing the returned
  items from invoking `$service` for each input list item.

---

### tundra.list.object:prepend

Prepends a single item to the front of a list, such that prepending
an item to a list containing n items results in a new list of n + 1
items.

#### Inputs:

* `$list` is a list to be prepended to.
* `$item` is the item to prepend to the given list.
* `$class` is an optional Java class name that the list and item
  to be prepended are required to be instances of.

#### Outputs:

* `$list` is the resulting list with the given `$item` prepended to the
  start.

---

### tundra.list.object:put

Sets the value of the item at the given index in the given list.

#### Inputs:

* `$list` is a list in which to set the given value.
* `$item` is the item to be set in the given list.
* `$index` is an optional zero-based index identifying
  the item to be set. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).
* `$class` is an optional Java class name that the list
  and item are required to be instances of.

#### Outputs:

* `$list` is the resulting list with the item at the given
  index set to the given value.

---

### tundra.list.object:reject

Filters the given list to not include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to not include items
  equal to 1 or 2, use the following `$condition`:

      %$item% == 1 or %$item% == 2

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to not include the
  items where `$condition` evaluated to true.

---

### tundra.list.object:resize

Resizes the given list to the given length, truncated from the end when the
length is decreased, and padded with the given item (or null if not
specified) when the length is increased.

#### Inputs:

* `$list` is the Object[] list to be grown.
* `$item` is an optional Object used to pad the list if increasing the
  size.
* `$length` is the desired new length of the list if specified as a
  positive integer, or the relative number of items to truncate from the
  list if specified as a negative integer.
* `$class` is an optional Java class name used to instantiate a new list
  if the provided `$list` is null.

#### Outputs:

* `$list` is the Object[] list resized to the desired `$length`; if the
  new length is less than the original length, the list is truncated from
  the end; if the new length is greater than the original length, the
  list is padded with `$item` (or null if not specified).

---

### tundra.list.object:reverse

Returns a new list with all items from the given list in
reverse order.

#### Inputs:

* `$list` is the list to be reversed.

#### Outputs:

* `$list` is the given list with item ordering reversed.

---

### tundra.list.object:shrink

Decreases the size of the given list by the given count, truncating items
from the end.

#### Inputs:

* `$list` is the Object[] list to be shrunk.
* `$count` is the number of items to truncate from the end of the list.

#### Outputs:

* `$list` is the Object[] list shrunk from the end of the list by the
  desired item count by truncating items from the end of the list.

---

### tundra.list.object:slice

Returns a new list which is a subset of the items in the
given list.

#### Inputs:

* `$list` is the list to be sliced.
* `$index` is the zero-based start index from which to
  take the slice.
* `$length` is the number of items to include in the
  slice.

#### Outputs:

* `$list` is the desired subset or slice of the given list.

---

### tundra.list.object:sort

Returns a new list sorted according to the [natural ordering] of
the given list's items.

#### Inputs:

* `$list` is the list to be sorted.

#### Outputs:

* `$list` is the sorted list.

---

### tundra.list.object:squeeze

Trims all leading and trailing whitespace from all string values,
then converts empty strings to nulls, then compacts the list by
removing all null values.

#### Inputs:

* `$list` is an object list to be squeezed.

#### Outputs:

* `$list` is the resulting object list with all string values
  trimmed of leading and trailing whitespace characters, and
  all empty string values and null values removed.

---

### tundra.list.object:unique

Returns a new list with all duplicates from the given list
removed, such that no two items are equal.

#### Inputs:

* `$list` is the list to process.

#### Outputs:

* `$list` is the resulting list with all duplicate items
  removed.

---

### tundra.list.service:chain

Invokes each service in the given list sequentially, sharing the pipeline
across all invokes.

#### Inputs:

* `$services` is a list of fully-qualified service names identifying the
  services to be invoked sequentially.
* `$pipeline` is an optional scoped pipeline to use when invoking the
  services. If not specified, the pipeline itself is used.

#### Outputs:

* `$pipeline` is the output pipeline of the invocations of `$services`. This
  is only returned if `$pipeline` was specified as an input. Otherwise, the
  outputs of these invocations are merged directly with the pipeline
  itself.
* `$duration` is the total execution duration for all services.

---

### tundra.list.service:ensure

Provides a try/catch/finally pattern for chained flow services. Each
service in the given list is invoked sequentially, and the pipeline
is shared across all invokes.

#### Inputs:

* `$services` is a list of fully-qualified service names
  identifying the services to be invoked sequentially in
  the [try block].
* `$catch` is an optional fully-qualified name of a service
  invoked in the [catch block]. This service should
  implement the `Tundra/tundra.schema.exception:handler`
  specification. If no `$catch` service is specified, the
  exception will be rethrown.
* `$finally` is an optional fully-qualified name of a service
  invoked in the [finally block] (always invoked regardless
  of whether an exception is thrown).
* `$pipeline` is an optional scoped pipeline to use when
  invoking the services. If not specified, the pipeline
  itself is used.

#### Outputs:

* `$pipeline` is the output pipeline of the invocations of
  `$services`, `$catch`, and `$finally`. This is only returned if
  `$pipeline` was specified as an input. Otherwise, the outputs
  of these invocations are merged directly with the pipeline
  itself.

---

### tundra.list.service:invoke

Invokes a list of services either synchronously (with an optional
level of concurrency) or asynchronously.

#### Inputs:

* `$invocations` is a list of services and pipelines to be invoked.
  * `service` is the fully-qualified name of the service to be
    invoked.
  * `pipeline` is an optional IData document which, if specified,
    contains the input arguments for the invocation of `service`;
    in other words, the invocation is scoped to this IData document.
    If not specified, the invocation is unscoped, and hence the
    service will operate directly against the pipeline itself.
* `$mode` is an optional choice of execution mode: either synchronous
  or asynchronous. When synchronous, the invocation of
  `Tundra/tundra.list.service:invoke` blocks until all invocations
  complete. When asynchronous, `Tundra/tundra.list.service:invoke`
  returns immediately and the invocations occur on other threads.
* `$concurrency` is an optional number of threads used when invoking
  synchronously. Defaults to 1.

#### Outputs:

* `$invocations` is the resulting list of services, output pipelines,
  and threads (when invoked in asynchronous mode).
  * `service` is the fully-qualified name of the service invoked.
  * `pipeline` is the output pipeline of the invocation of
    `service`. This is only returned if the `$mode` is synchronous,
    and the invocation was scoped (`$invocations/pipeline` was specified
    as an input). If invocation was synchronous and unscoped, the
    outputs of the invocation are merged directly with the pipeline
    itself. If the invocation was asynchronous, then no outputs
    are returned.
  * `thread` is returned only when the invocation `$mode` is
    asynchronous, and is the thread object which can later be waited on
    to finish using `Tundra/tundra.list.service:join`.

---

### tundra.list.service:join

Waits for each service thread in the given list to finish before
returning each output pipeline.

* Inputs
* `$threads` is a list of service thread objects returned by
  `Tundra/tundra.list.service:invoke` when operating in the
  asynchronous invocation mode.

#### Outputs:

* `$pipelines` is a list of all the output pipelines returned
  by the service threads.

---

### tundra.list.string:append

Appends a single item to the end of a list, such that appending an item
to a list containing n items results in a new list of n + 1 items.

#### Inputs:

* `$list` is a list to append an item to.
* `$item` is an item to be appended to the given list.

#### Outputs:

* `$list` is the resulting list with the given `$item` appended to the
  end.

---

### tundra.list.string:coalesce

Returns the first string in the given list that is not null, or if
all strings in the list are null then the given item.

#### Inputs:

* `$list` is an optional list of strings.
* `$default` is an optional string, included for convenience when using
  this service in a map step and providing a default value.
* `$mode` determines what is returned when all arguments are null:
  * `missing`: `$item` is not returned when all arguments are null.
    This is the default, if `$mode` is not specified.
  * `null`: `$item` is returned as null when all arguments are null.

#### Outputs:

* `$item` is the first string in the given `$list` that is not null,
  or the given input `$item` if it is not null, or null if all
  other values were null and `$mode` is 'null'.

---

### tundra.list.string:compact

Removes all null items from the given list, thereby shortening the
length of the list.

#### Inputs:

* `$list` is a list to be compacted.

#### Outputs:

* `$list` is the given list with all null items removed.

---

### tundra.list.string:concatenate

Returns a new list containing all the items in the given lists.

#### Inputs:

* `$list.x` is the first list to be concatenated.
* `$list.y` is the second list to be concatenated.

#### Outputs:

* `$list` is a new list containing all the items from the given
  input lists.

---

### tundra.list.string:difference

Returns a list of only the items in `$list.x` that are not also
present in `$list.y`.

#### Inputs:

* `$list.x` is a list to be checked for differences against `$list.y`.
* `$list.y` is a list to be checked against `$list.x` for differnces.

#### Outputs:

* `$list` is a new list containing only the items in `$list.x` that
  are not also present in `$list.y`.

---

### tundra.list.string:drop

Removes the item stored at a given index in the given list.

#### Inputs:

* `$list` is a list to remove the item from.
* `$index` is an integer identifying which item to be removed from
  the given list. List indexing is zero-based. Supports both
  forward and reverse indexing (where, for example, an index of
  -1 is the last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the given list with the item identified by the given
  index removed.

---

### tundra.list.string:each

Iterates through the given list, invoking the given service for each
item in the list, passing `$item`, `$index`, `$iteration` and `$length`
variables.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking `$service`.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of `$service`. Defaults to
  $item.

---

### tundra.list.string:equal

Returns true if the two given lists are equal.

#### Inputs:

* `$list.x` is a list to be compared with `$list.y`.
* `$list.y` is a list to be compared with `$list.x`.

#### Outputs:

* `$equal?` is a boolean indicating if `$list.x` equals
  `$list.y`.

---

### tundra.list.string:filter

Filters the given list to only include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to only include items
  equal to a regular expression pattern, use the following
  `$condition`:

      %$item% == /\d\d/

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to only include the
  items where `$condition` evaluated to true.

---

### tundra.list.string:find

Returns the list of items which included at least one occurrence of the
given [regular expression pattern] or literal string pattern, and the list
of items which did not include any occurrences of the pattern.

#### Inputs:

* `$list` is a list to be searched for the given pattern.
* `$pattern` is the [regular expression pattern] or literal string to
  search for against the given list.
* `$literal?` is a boolean indicating if the `$pattern` string should be
  treated as a literal string. If false, `$pattern` is treated as a
  [regular expression pattern]. If true, `$pattern` is treated as a literal string. Defaults to false, if not specified.


#### Outputs:

* `$found.all?` is a boolean flag which when true indicates that all
  items in the given list included the given pattern.
* `$found.any?` is a boolean flag which when true indicates that at least
  one item in the given list included the given pattern.
* `$found.none?` is a boolean flag which when true indicates that no
  items in the given list included the given pattern.
* `$found` is the list of items which included the given pattern.
* `$found.length` is the number of items in the `$found` list.
* `$unfound` is the list of items which did not include the given
  pattern.
* `$unfound.length` is the number of items in the `$unfound` list.

---

### tundra.list.string:first

Returns the first item from the given list.

#### Inputs:

* `$list` is a list to fetch the first item from.

#### Outputs:

* `$item` is the first item in the given list.

---

### tundra.list.string:get

Returns the item stored at a given index in a list. A zero-
based index can be specified using the `$index` input, or
a one-based index can be specified using the `$iteration` input
(which is useful when using this service inside a flow loop).

#### Inputs:

* `$list` is a list to fetch an item from.
* `$index` is an optional zero-based index identifying
  the item to be fetched. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).
* `$iteration` is an optional one-based index identifying
  the item to be fetched.

#### Outputs:

* `$item` is the item stored at the given index in the given
  list.

---

### tundra.list.string:grow

Increases the size of the given list by the given count of items, padded
with the given item (or null if not specified).

#### Inputs:

* `$list` is the String[] list to be grown.
* `$item` is an optional String used to pad the newly grown section of
  the list with.
* `$count` is the number of new items to add to the list.

#### Outputs:

* `$list` is the String[] list grown by the desired `$count` of items,
  with the original items preserved and the new items padded with `$item`
  (or null if not specified).

---

### tundra.list.string:include

Returns true if the given item is found in the given list.

#### Inputs:

* `$list` is a list to check whether the given `$item` exists
  in.
* `$item` is the item to be checked against the given `$list`.

#### Outputs:

* `$include?` is a boolean indicating the the given `$item`
  exists as an item in the given `$list`.

---

### tundra.list.string:insert

Returns a new list with the given item inserted at the desired
index in the given list.

#### Inputs:

* `$list` is a list to check whether the given `$item` exists
  in.
* `$item` is the item to be checked against the given `$list`.
* `$index` is the index at which to insert the item. List
  indexing is zero-based. Supports both forward and
  reverse indexing (where, for example, an index of -1
  is the last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the resulting list with the item inserted at the
  desired index.

---

### tundra.list.string:intersection

Returns a list of only the items in `$list.x` that are also present in
`$list.y`.

#### Inputs:

* `$list.x` is a list to be intersected with `$list.y`.
* `$list.y` is a list to be intersected with `$list.x`.

#### Outputs:

* `$list` is a new list containing only the items that are present
  in both input lists (the [set intersection]).

---

### tundra.list.string:join

Returns a string created by concatenating the list items together,
separated by the given $separator.

#### Inputs:

* `$list` is a list to be converted to a string.
* `$separator` is an optional string used to separate each list item
  in the resulting string. Defaults to an empty string.

#### Outputs:

* `$result` is a string containing each item in the given list, converted
  to a string and separated by the given `$separator`.

---

### tundra.list.string:last

Returns the last item from the given list.

#### Inputs:

* `$list` is a list to fetch the last item from.

#### Outputs:

* `$item` is the last item in the given list.

---

### tundra.list.string:length

Returns the number of items in the given list.

#### Inputs:

* `$list` is a list to count the number of items in.

#### Outputs:

* `$length` is the number of items in the given list.

---

### tundra.list.string:lowercase

Converts all strings in the given list to lower case.

#### Inputs:

* `$list` is a list of strings to be converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.

#### Outputs:

* `$list` is the resulting list of lower case strings, where
  all characters have been converted to their lower case
  equivalent according to the transformation rules of the given
  [Locale].

---

### tundra.list.string:map

Returns a new list created by invoking the given service for each
item in the input list, passing `$item`, `$index`, `$iteration` and
`$length` variables, and collecting the values returned by the service
to form the new list.

This is an implementation of a higher-order [map function] for
string lists.

#### Inputs:

* `$list` is a list to be iterated over.
* `$service` is a fully-qualified service name identifying the
  service to be invoked to process each item in the list.
* `$pipeline` is an optional IData document containing arbitrary
  input arguments used when invoking `$service`.
* `$item.input` is an optional variable name used when passing each
  item in the list to the invocation of `$service`. Defaults to
  $item.
* `$item.output` is an optional variable name used when extracting
  the resulting item from the invocation of `$service`. Defaults to
  $item.

#### Outputs:

* `$list` is the newly constructed list containing the returned
  items from invoking `$service` for each input list item.

---

### tundra.list.string:match

Returns the list of items which matched and the list of items which did
not match the given [regular expression pattern] or literal string pattern.

#### Inputs:

* `$list` is a list to be matched against the given pattern.
* `$pattern` is the [regular expression pattern] or literal string to
  match against the given list.
* `$literal?` is a boolean indicating if the `$pattern` string should be
  treated as a literal string. If false, `$pattern` is treated as a
  [regular expression pattern]. If true, $pattern is treated as a
  literal string. Defaults to false, if not specified.

#### Outputs:

* `$matched.all?` is a boolean flag which when true indicates that all
  items in the given list matched the given pattern.
* `$matched.any?` is a boolean flag which when true indicates that at
  least one item in the given list matched the given pattern.
* `$matched.none?` is a boolean flag which when true indicates that no
  items in the given list matched the given pattern.
* `$matched` is the list of items which matched the given pattern.
* `$matched.length` is the number of items in the `$matched` list.
* `$unmatched` is the list of items which did not match the given
   pattern.
* `$unmatched.length` is the number of items in the `$unmatched` list.

---

### tundra.list.string:normalize

Converts a list of strings, byte arrays or input streams to a
list of strings.

#### Inputs:

* `$objects` is a list of strings, byte arrays, or input streams
  to be converted to a string.
* `$encoding` is an optional character set to use when `$objects`
  is a list of byte arrays or input streams. Defaults to [UTF-8].

#### Outputs:

* `$strings` is the resulting list of strings.

---

### tundra.list.string:prepend

Prepends a single item to the front of a list, such that prepending
an item to a list containing n items results in a new list of n + 1
items.

#### Inputs:

* `$list` is a list to be prepended to.
* `$item` is the item to prepend to the given list.

#### Outputs:

* `$list` is the resulting list with the given `$item` prepended to the
  start.

---

### tundra.list.string:put

Sets the value of the item at the given index in the given list.

#### Inputs:

* `$list` is a list in which to set the given value.
* `$item` is the item to be set in the given list.
* `$index` is an optional zero-based index identifying
  the item to be set. Supports forward and reverse
  indexing (where, for example, an index of -1 is the
  last item in the list, and an index of -2 is the
  second last item in the list).

#### Outputs:

* `$list` is the resulting list with the item at the given
  index set to the given value.

---

### tundra.list.string:quote

Returns a [regular expression pattern] that can be used to match any
of the given strings literally.

#### Inputs:

* `$list` is the list of strings to be converted to a [regular expression
  pattern].


#### Outputs:

* `$pattern` is a [regular expression pattern] that can be used to match
  the any of the given `$list` strings literally.

---

### tundra.list.string:reject

Filters the given list to not include items where the
given condition evaluates to true.

#### Inputs:

* `$list` is the list to be filtered.
* `$condition` is a `tundra.condition:evaluate` compatible
  conditional statement used to filter the given list.

  List items are represented in the evaluation scope by
  a variable named `$item`.

  For example, to filter a list to not include items
  equal to a regular expression pattern, use the following
  `$condition`:

      %$item% == /\d\d/

* `$scope` is an optional IData document containing the
  variables against which `$condition` will be evaluated.
  If not specified, the `$condition` will be evaluated
  against the pipeline.

#### Outputs:

* `$list` is the given list filtered to not include the
  items where `$condition` evaluated to true.

---

### tundra.list.string:replace

Replaces all occurrences of the given [regular expression pattern]
in the given list of strings, with the replacement string.

#### Inputs:

* `$list` is a list of strings to have all occurrences of the given
  [regular expression pattern] replaced.
* `$pattern` is the [regular expression pattern] to match against
  the given string.
* `$replacement` is the replacement string to be substituted in
  the given string wherever the given pattern is found.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.

#### Outputs:

* `$list` is the input list of strings with all occurrences of the given
  [regular expression pattern] replaced with `$replacement`.

---

### tundra.list.string:resize

Resizes the given list to the given length, truncated from the end when the
length is decreased, and padded with the given item (or null if not
specified) when the length is increased.

#### Inputs:

* `$list` is the String[] list to be grown.
* `$item` is an optional String used to pad the list if increasing the
  size.
* `$length` is the desired new length of the list if specified as a
  positive integer, or the relative number of items to truncate from the
  list if specified as a negative integer.

#### Outputs:

* `$list` is the String[] list resized to the desired `$length`; if the
  new length is less than the original length, the list is truncated from
  the end; if the new length is greater than the original length, the
  list is padded with `$item` (or null if not specified).

---

### tundra.list.string:reverse

Returns a new list with all items from the given list in
reverse order.

#### Inputs:

* `$list` is the list to be reversed.

#### Outputs:

* `$list` is the given list with item ordering reversed.

---

### tundra.list.string:shrink

Decreases the size of the given list by the given count, truncating items
from the end.

#### Inputs:

* `$list` is the String[] list to be shrunk.
* `$count` is the number of items to truncate from the end of the list.

#### Outputs:

* `$list` is the String[] list shrunk from the end of the list by the
  desired item count by truncating items from the end of the list. If
  the list is smaller than the count, an empty list is returned.

---

### tundra.list.string:slice

Returns a new list which is a subset of the items in the
given list.

#### Inputs:

* `$list` is the list to be sliced.
* `$index` is the zero-based start index from which to
  take the slice.
* `$length` is the number of items to include in the
  slice.

#### Outputs:

* `$list` is the desired subset or slice of the given list.

---

### tundra.list.string:sort

Returns a new list sorted according to the [natural ordering] of
the given list's items.

#### Inputs:

* `$list` is the list to be sorted.

#### Outputs:

* `$list` is the sorted list.

---

### tundra.list.string:squeeze

Replaces runs of one or more whitespace characters (space, tab,
carriage return, line feed) with a single space character in
the given list of strings.

#### Inputs:

* `$list` is a list of strings to squeeze extraneous whitespace
  from.

#### Outputs:

* `$list` is the input list of strings with extraneous whitespace
  characters removed and replaced with a single space character.

---

### tundra.list.string:substitute

Attempts variable substitution on each string in the given list
by replacing all occurrences of substrings matching "%key%" with
the associated (optionally scoped) value.

Optionally replaces null or non-existent values with the given
default value.

#### Inputs:

* `$list` is a list of strings to perform variable substitution on.
* `$pipeline` is an optional IData document used to scope the
  variable substitution. If not specified, the substitution
  is unscoped (resolved against the pipeline itself).
* `$default` is an optional default value to substitute in place
  of null or missing values.

#### Outputs:

* `$list` is the input list of strings with variable substitution
  patterns, such as "%key%", replaced with the value of the key
  (resolved against either `$pipeline`, if specified, or the pipeline
  itself).

---

### tundra.list.string:trim

Removes leading and trailing whitespace from each string in the
given list.

#### Inputs:

* `$list` is a list of strings to be trimmed of leading and trailing
  whitespace characters.

#### Outputs:

* `$list` is the input list of strings with leading and trailing
  whitespace characters removed.

---

### tundra.list.string:unique

Returns a new list with all duplicates from the given list
removed, such that no two items are equal.

#### Inputs:

* `$list` is the list to process.

#### Outputs:

* `$list` is the resulting list with all duplicate items
  removed.

---

### tundra.list.string:uppercase

Converts all strings in the given list to upper case.

#### Inputs:

* `$list` is a list of strings to be converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.

#### Outputs:

* `$list` is the resulting list of upper case strings, where
  all characters have been converted to their upper case
  equivalent according to the transformation rules of the given
  [Locale].

---

### tundra.mime.type:emit

Emits a Multipurpose Internet Mail Extension ([MIME]) type,
according to [RFC 2045] and [RFC 2046], given its constituent
parts.

Implemented with the [javax.activation.MimeType] class.

#### Inputs:

* `$type` is an optional [mime type] data structure of the
  constituent parts that make a [mime type].

#### Outputs:

* `$string` is resulting [mime type] in its canonical string
  representation.

---

### tundra.mime.type:equal

Returns true if the given [mime type] strings are considered equal.

#### Inputs:

* `$string.x` is an optional [mime type] string to be compared
  with `$string.y`.
* `$string.y` is an optional [mime type] string to be compared
  with `$string.x`.

#### Outputs:

* `$equal?` is true if the given [mime type] strings are considered
  equal (their base and sub types both match).

---

### tundra.mime.type:normalize

Normalizes a mime type by removing extraneous whitespace characters,
and listing parameters in alphabetical order.

Implemented with the [javax.activation.MimeType] class.

#### Inputs:

* `$string` is an optional [mime type] string to be normalized.

#### Outputs:

* `$string` is the normalized [mime type] string.

---

### tundra.mime.type:parse

Parses a Multipurpose Internet Mail Extension ([MIME]) type, according
to [RFC 2045] and [RFC 2046], into its constituent parts.

Implemented with the [javax.activation.MimeType] class.

#### Inputs:

* `$string` is an optional [mime type] string to be parsed.

#### Outputs:

* `$type` is an IData data structure representing the constituent
  parts of a [mime type] string.

---

### tundra.mime.type:validate

Returns true if the given string can be parsed as a valid Multipurpose
Internet Mail Extension ([MIME]) type, according to [RFC 2045] and
[RFC 2046].

Implemented with the [javax.activation.MimeType] class.

#### Inputs:

* `$string` is an optional [mime type] string to be validated.
* `$raise?` is an optional boolean indicating whether to throw an
  exception if the given string is not a valid [mime type] string.
  Defaults to false.

#### Outputs:

* `$valid?` is a boolean indicating if the given string is a valid
  [mime type] string.

---

### tundra.node:access

Grants the specified permissions on the given namespace node.

#### Inputs:

* `$node` is the Integration Server namespace node to grant
  permissions on, provided as a normal namespace identifer:

      folder(.folder)*(:object)

  Where brackets indicate optional components of the namespace
  identifer, '*' indicates 0 or more occurrences of a
  component, '.' and ':' are literal characters.
* `$permissions` is the list of permissions to be granted on
  the given `$node`.
  * `type` identifies the type of permission to be granted, one of:
    * `list` allows members of the given ACL to see the child items
      in the given node
    * `read` allows members of the given ACL to view the given node
    * `write` allows members of the given ACL to change the given node
    * `execute` allows members of the given ACL to execute the given node
  * `acl` is the name of the ACL object to be granted permission on
    this namespace node.

---

### tundra.node:exists

Returns true if the given namepace node exists.

#### Inputs:

* `$node` is the Integration Server namespace node to check
  existence of, provided as a normal namespace identifer:

      folder(.folder)*(:object)

  Where brackets indicate optional components of the namespace
  identifer, '*' indicates 0 or more occurrences of a
  component, '.' and ':' are literal characters.

#### Outputs:

* `$exists?` is a boolean flag indicating if the given node
  exists on this Integration Server.

---

### tundra.node:list

Lists an interface's child nodes. Optionally filters based on
the given regular expression pattern, and node type.

#### Inputs:

* `$interface` is the Integration Server namespace folder to check
  existence of, provided as a normal namespace interface node:

      folder(.folder)*

  Where brackets indicate optional components of the namespace
  identifer, and '*' indicates 0 or more occurrences of a
  component.
* `$pattern` is an optional regular expression pattern used to
  filter the returned list of nodes (only nodes whose name
  matches the given pattern are returned).
* `$type` is an optional node type used to filter the returned
  list of nodes (only nodes whose type matches are returned).
* `$recurse?` is an optional boolean flag indicating whether to
  recursively list all items in all child folders/interfaces.
  Defaults to false.

#### Outputs:

* `$nodes` is the list of (optionally filtered) child nodes that
  exist as items within the given `$interface`.

---

### tundra.node:type

Returns the type of the given node, such as whether it is an
interface, service, or record.

#### Inputs:

* `$node` is the Integration Server namespace node to check
  existence of, provided as a normal namespace identifer:

      folder(.folder)*(:object)

  Where brackets indicate optional components of the namespace
  identifer, '*' indicates 0 or more occurrences of a
  component, '.' and ':' are literal characters.

#### Outputs:

* `$type` is the type of node that `$node` is, such as interface,
  or service.

---

### tundra.packages:exist

Returns true if a package with the given name exists on this
Integration Server.

#### Inputs:

* `$name` is the name of the package to check existence of.

#### Outputs:

* `$exists?` is a boolean indicating whether a package with
  the given name exists on this Integration Server.

---

### tundra.packages:get

Returns information about the package with the given name.

#### Inputs:

* `$name` is the name of the package to return information
  about.

#### Outputs:

* `$package` contains the following information about the package
  with the given name if it exists:
  * `name` is the name of the package.
  * `version` is the version of the package as per its manifest.
  * `enabled?` is a boolean indicating if the package is enabled.
  * `system?` is a boolean indicating if the package is considered
    a system package.
  * `dependencies` is a list of all the packages this package is
    dependent on as per its manifest:
    * `package` is the name of the dependent package.
    * `version` is the version of the dependent package.
  * `dependencies.length` is the number of items in the
    dependencies list.
  * `services` lists all the different type of services in the
    package:
    * `startup` is a list of all the registered startup services in
      the package.
    * `startup.length` is the number of items in the `startup` list.
    * `shutdown` is a list of all the registered shutdown services
      in the package.
    * `shutdown.length` is the number of items in the `shutdown` list.
    * `replication` is a list of all the registered replication
      services in the package.
    * `replication.length` is the number of items in the `replication`
      list.
    * `loaded` is a list of all the loaded services in the package.
    * `loaded.length` is the number of items in the `loaded` list.
  * `directories` provides the location of all the well-known
    directories for the package:
    * `root` is the package's root directory.
    * `ns` is the package's namespace directory.
    * `pub` is the package's public HTML directory.
    * `template` is the package's service template directory.
    * `web` is the package's Java web application directory.
    * `config` is the package's configuration directory.
* `$exists?` is a boolean indicating whether a package with
  the given name exists on this Integration Server.

---

### tundra.packages:list

Returns a list of either all known packages or only those enabled on
this Integration Server.

#### Inputs:

* `$enabled?` is an optional boolean indicating whether to return
  only packages which are enabled on this Integration Server.
  Defaults to `false`.

#### Outputs:

* `$packages` is the list of either all known packages or only those
  enabled on this Integration Server:
  * `name` is the name of the package.
  * `version` is the version of the package as per its manifest.
  * `enabled?` is a boolean indicating if the package is enabled.
  * `system?` is a boolean indicating if the package is considered
    a system package.
  * `dependencies` is a list of all the packages this package is
    dependent on as per its manifest:
    * `package` is the name of the dependent package.
    * `version` is the version of the dependent package.
  * `dependencies.length` is the number of items in the
    `dependencies` list.
  * `services` lists all the different type of services in the
    package:
    * `startup` is a list of all the registered startup services in
      the package.
    * `startup.length` is the number of items in the `startup` list.
    * `shutdown` is a list of all the registered shutdown services
      in the package.
    * `shutdown.length` is the number of items in the `shutdown` list.
    * `replication` is a list of all the registered replication
      services in the package.
    * `replication.length` is the number of items in the `replication`
      list.
    * `loaded` is a list of all the loaded services in the package.
    * `loaded.length` is the number of items in the `loaded` list.
  * `directories` provides the location of all the well-known
    directories for the package:
    * `root` is the package's root directory.
    * `ns` is the package's namespace directory.
    * `pub` is the package's public HTML directory.
    * `template` is the package's service template directory.
    * `web` is the package's Java web application directory.
    * `config` is the package's configuration directory.
* `$packages.length` is the number of items in the `$packages` list.

---

### tundra.object:coalesce

Returns the first object argument that is not null.

#### Inputs:

* `$object.x` is an optional object argument.
* `$object.y` is an optional object argument.
* `$mode` determines what is returned when all
  arguments are null:
  * `missing`: `$object` is not returned when all
    arguments are null. This is the default, if
    `$mode` is not specified.
  * `null`: `$object` is returned as null when all
    arguments are null.

#### Outputs:

* `$object` is the first of the given arguments
  whose value is not null, or null if all
  arguments were null and `$mode` is 'null'.

---

### tundra.object:convert

Converts a string, byte array, or input stream object to a string,
byte array or input stream object.

#### Inputs:

* `$object` is an optional string, byte array, or input stream
  object. If null, this service does nothing.
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string', which determines the type of object returned by
  this service. Defaults to 'stream'.
* `$encoding` is an optional character set to use when converting
  from or to a string. Defaults to [UTF-8].

#### Outputs:

* `$object` is the input object converted to be either a string,
  byte array, or input stream as determined by the selected
  `$mode`, or null if the input object was null.

---

### tundra.object:equal

Returns true if the two given objects are equal.

#### Inputs:

* `$object.x` is an optional object of any class to be compared
  to `$object.y`.
* `$object.y` is an optional object of any class to be compared
  to `$object.x`.

#### Outputs:

* `$equal?` is true if the two input objects are considered
  equivalent.

---

### tundra.object:instance

Returns true if the given object is an instance of the given class.

#### Inputs:

* `$object` is an optional object of any class.
* `$class` is a Java class name.

#### Outputs:

* `$instance?` is true if the give object is an instance of the given
  Java class.

---

### tundra.object:listify

Converts the value or values identified by the given key to a new
list containing the original value or values as its items, unless
the original value is already a list in which case it remains
unmodified.

#### Inputs:

* `$key` is a simple or fully-qualified (such as `a/b/c[0]/d`) key
  identifying the value to be converted to a list.
* `$scope` is an optional `IData` document that, if specified, is used
  to resolve the given `$key` against. If not specified, `$key` is
  resolved against the pipeline.

#### Outputs:

* `$scope` is returned if an input `$scope` was provided, where the value
  identified by `$key` within it has been converted to a list. If
  no input `$scope` was specified, the value identified by `$key` in the
  pipeline is instead converted to a list. If `$key` does not identify
  any value, this service does nothing.

---

### tundra.object:nothing

Returns null.


#### Outputs:

* `$nothing` is returned with a null value.

---

### tundra.object:reflect

Returns whether the given object is an array, a primitive type or an
array of a [primitive type], and its Java class name.

#### Inputs:

* `$object` is an optional object to be reflected on. If neither input
  is specified, this service does nothing and no outputs are returned.
* `$key` is an optional key identifying the value in the pipeline to
  relect on, and can be simple or fully qualified, such as `a/b/c[0]/d`.
  Use `$key` instead of `$object` when reflecting on an object that could
  be a list/array, because mapping a list/array to `$object` will only
  map the first item in the list. If neither input is specified, this
  service does nothing and no outputs are returned.

#### Outputs:

* `$id` is the identity hash code of the given object, as calculated by
  the [java.lang.System.identityHashCode()] method. The identity hash
  code for the null reference is always zero.
* `$class` is the Java class name of the given object.
* `$array?` is a boolean indicating if the given object is an array.
* `$primitive?` is a boolean indicating if the given object is a
  [primitive type], or an array of a [primitive type].

---

### tundra.object:stringify

Converts the given object to its string representation by calling
[Object.toString()].

#### Inputs:

* `$object` is an optional object to be converted to its string
  representation.

#### Outputs:

* `$string` is the given object converted to its string
  representation.

---

### tundra.pipeline:capture

Clones the pipeline and returns it.  This is useful if you want
to use the pipeline itself as a document, which you can then pass
as input when calling a service for example.


#### Outputs:

* `$pipeline` is the captured pipeline as an IData document.

---

### tundra.pipeline:clear

Removes all elements from the pipeline, except for any keys
specified in the preserve list.

#### Inputs:

* `$preserve` is a list of keys (simple or fully qualified,
  such as `a/b/c[0]/d`) to not be dropped from the pipeline.
  All other keys not in this list will be dropped.

---

### tundra.pipeline:copy

Copies the value associated with the source key to the target
key in the pipeline.

#### Inputs:

* `$key.source` identifies the value to be copied, and can be
  simple or fully qualified, such as `a/b/c[0]/d`.
* `$key.target` is the key to copy the source value to, and
  can be simple or fully qualified, such as `a/b/c[0]/d`.

---

### tundra.pipeline:drop

Drops the key value pair associated with the given key from
the pipeline.

#### Inputs:

* `$key` identifies the key value pair to be dropped, and
  can be simple or fully qualified, such as `a/b/c[0]/d`.

---

### tundra.pipeline:emit

Emits (or encodes) the current pipeline as an [IData XML] string,
byte array, or input stream.

#### Inputs:

* `$encoding` is an optional character set to use when `$content` is
  returned as an input stream or byte array. Defaults to [UTF-8].
* `$mode` is an optional choice of stream, bytes, or string,
  and determines the type of content object returned.

#### Outputs:

* `$content` is an input stream, byte array, or string (depending on
  the `$mode` selected) representing the encoded pipeline data.

---

### tundra.pipeline:first

Returns the first key value pair from the pipeline.


#### Outputs:

* `$key` is the first key in the pipeline.
* `$value` is the first key's associated value.

---

### tundra.pipeline:get

Returns the value associated with the given key from the
pipeline, or null if it doesn't exist.

#### Inputs:

* `$key` identifies the value to be retrieved from the pipeline,
  and can be simple or fully qualified, such as `a/b/c[0]/d`.

#### Outputs:

* `$value` is the value associated with the given `$key`.

---

### tundra.pipeline:last

Returns the last key value pair from the pipeline.


#### Outputs:

* `$key` is the last key in the pipeline.
* `$value` is the last key's associated value.

---

### tundra.pipeline:length

Returns the number of top-level elements in the pipeline.


#### Outputs:

* `$length` is the number of top-level elements in the
  pipeline.

---

### tundra.pipeline:listify

Converts the value or values identified by the given key to a new
list containing the original value or values as its items, unless
the original value is already a list in which case it remains
unmodified.

#### Inputs:

* `$key` is a simple or fully-qualified (such as `a/b/c[0]/d`) key
  identifying the value to be converted to a list.

---

### tundra.pipeline:log

Writes the current pipeline to the server log.

#### Inputs:

* `$level` is the logging level used when writing the
  pipeline contents to the server log.

---

### tundra.pipeline:merge

Merges the elements in the given IData document into the
pipeline.

#### Inputs:

* `$document` is an IData document whose top-level elements
  are to be copied directly into the pipeline.

---

### tundra.pipeline:normalize

Iterates over each element in the pipeline, deconstructing
all fully qualified keys into their constituent parts.

For example, if a pipeline contains the following key value
pairs (using [JSON] notation to represent the pipeline):

    {
      "a/b/c": "example 1",
      "a/b/d": "example 2",
      "e": "example 3",
      "f[0]": "example 4",
      "f[1]": "example 5"
    }

This is normalized to the following:

    {
      "a": {
        "b": {
          "c": "example 1",
          "d": "example 2"
        }
      },
      "e": "example 3",
      "f": ["example 4", "example 5"]
    }

Keys using path-style notation, for example "a/b/c", are
converted to nested IData documents with the final key
in the path, "c" in this example, assigned the value of
the original key.

Keys using array- or list-style notation, for example "f[0]",
are converted to an array or list with the value of the
original key assigned to the indexed item (the zeroth item in
this example).

---

### tundra.pipeline:parse

Parses (or decodes) the given [IData XML] string, byte array,
or input stream and merges it into the pipeline.

#### Inputs:

* `$content` is a string, byte array, or input stream containing
  [IData XML] data to be parsed and merged into the pipeline.
* `$encoding` is an optional character set to use when `$content` is
  provided as a byte array or input stream. Defaults to [UTF-8].

---

### tundra.pipeline:put

Sets the value associated with the given key in the pipeline.

#### Inputs:

* `$key` is a simple or fully qualified key (`a/b/c[0]/d`) to
  associate with the given `$value`.
* `$value` is the value to be associated with the given `$key`.

---

### tundra.pipeline:rename

Renames the value associated with the source key to have the
target key in the pipeline. After being renamed, the source
key will no longer exist in the pipeline.

#### Inputs:

* `$key.source` is a simple or fully qualified key (`a/b/c[0]/d`)
  to be renamed to the given target key.
* `$key.target` is the new simple or fully qualified key that the
  source key will be renamed to.

---

### tundra.pipeline:sort

Sorts the top-level elements in the pipeline by their keys in natural
ascending order.

---

### tundra.pipeline:substitute

Attempts variable substitution on every string element in the
pipeline by replacing all occurrences of substrings matching
"%key%" with the associated value.

---

### tundra.schedule:create

Schedules a service for execution once or periodically.

The resulting scheduled task ID is added to the input pipeline
of the scheduled service as a variable named `$schedule.id`,
enabling the service itself to query its owning scheduled task.
This can be useful if, for example, the service implements an
[event loop] and should exit that loop if its owning schedule
is paused or cancelled.

#### Inputs:

* `$schedule` is an IData document representing the task to be
  scheduled.
  * `type` describes the type of schedule to be created, and is
    a choice of the following:
    * `once` will schedule the service to execute one time only
      at the given `start` datetime.
    * `repeat` will schedule the service to execute periodically
      at the given `repeat/interval` of time.
    * `complex` will schedule the service to execute periodically
      on the given minutes, hours, week days, days of month, and
      months of the year.
  * `service` is the fully-qualified name of the service to be
    scheduled for execution.
  * `description` is an optional description of the scheduled task.
  * `target` optionally identifies the server or servers to execute
    the service on. Either specify a specific `hostname:port` to
    execute the service on, or one of the following values:
    * `$any` executes the task on any server in the cluster. Choose
      this option to ensure the service is only executed once at
      any one time, but can be executed by any server in the cluster.
    * `$all` execute the task on all servers in the cluster. Choose
      this option when the service needs to be executed on every
      cluster node at the same time to perform, for example, clean
      up or house keeping on every server.

    If not specified, will default to `$any` if the server is a
    member of a cluster, otherwise will default to execution on
    the server creating the scheduled task.
  * `user` is the optional user account under which the service will
    be executed. If not specified, the service will execute under
    the `Default` user account context.
  * `start` is the optional datetime from which the scheduled task
    will be in effect or active. If not specified, defaults to the
    current datetime. For `once` only tasks, this is the datetime
    the service will be executed once and only once.
  * `end` is the optional datetime after which the scheduled task
    will no longer be in effect (expires). If not specified, the
    schedule will never expire. Not applicable for `once` scheduled
    tasks.
  * `overlap?` is an optional boolean determining how to handle when
    one execution of the scheduled task overlaps the next scheduled
    execution, for example when the execution duration exceeds the
    schedule interval.

    When true, the next scheduled execution of the service will
    execute as per the schedule, even if the previous schedule is
    still executing, which can result in multiple concurrent
    executions of the service.

    When false, the schedule will never be executed concurrently.
    For a repeat schedule, the next scheduled time is calculated
    from the end of execution of the previous schedule. For a complex
    schedule, any scheduled times that occur while the previous
    schedule is executing are skipped, and the service will execute
    at the next uncontested scheduled time.
  * `lateness` is an optional IData document containing arguments for
    how to determine when, and then handle if, a schedule is late.
    * `duration` is an optional duration of time which when lapsed
      passed the scheduled time determines that the task is considered
      late. If not specified, defaults to 0 seconds (in other words a
      task is considered late if it doesn't execute exactly at its
      scheduled time).
    * `action` is an optional choice which determines what happens to
      the task when its determined to be late:
      * `run immediately`
      * `skip and run at next scheduled interval`
      * `suspend`

      If not specified, defaults to `run immediately`.
  * `repeat` is an optional IData document containing arguments only
    applicable when the schedule type is `repeat`.
    * `interval` is an optional duration of time determining how often
      the scheduled task will execute. If not specified, defaults to
      60 seconds.
  * `complex` is an optional IData document containing arguments only
    applicable when the schedule type is `complex`.
    * `months` is an optional list of months of the year, provided as
      an integer between 1 (January) and 12 (December), the schedule
      should execute in. If not specified, the schedule will execute
      in all months of the year.
    * `days` is an optional list of days of the month, provided as an
      integer between 1 and 31, the schedule should execute on. If
      not specified, the schedule will execute on all days in the
      month.
    * `weekdays` is an optional list of days of the week, provided as
      an integer between 1 (Sunday) and 7 (Saturday), the schedule
      should execute on. If not specified, the schedule will execute
      on all days of the week.
    * `hours` is an optional list of hours of the day, provided as an
      integer between 0 and 23, the schedule should execute on. If
      not specified, the schedule will execute every hour.
    * `minutes` is an optional list of minutes of the hour, provided as
      an integer between 0 and 59, the schedule should execute on. If
      not specified, the schedule will execute every minute.
  * `pipeline` is an optional IData document containing the input
    arguments used as the input pipeline when executing the service.
* `$singleton?` is an optional boolean which when true indicates that
  only one scheduled task should ever exist for this service, and
  therefore any existing scheduled tasks for this service will be
  first removed prior to creating a new scheduled task. Defaults
  to false.
* `$enabled?` is an optional boolean which when `true` indicates that
  the scheduled task should be created in an active state, and
  when `false` indicates that the scheduled task should be created
  in a suspended state. Defaults to `true`.

#### Outputs:

* `$id` uniquely identifies the resulting scheduled task.

---

### tundra.schedule:exists

Returns true if a scheduled task with the given `$id` exists in
the task scheduler on this Integration Server.

#### Inputs:

* `$id` is an optional string identifier.

#### Outputs:

* `$exists?` is a boolean indicating if a scheduled task
  identified by the given `$id` exists in the task scheduler
  of this Integration Server.

---

### tundra.schedule:get

Returns the details of the scheduled task identified by the given `$id`,
or nothing if no task with that `$id` exists.

#### Inputs:

* `$id` is an optional string identifier.

#### Outputs:

* `$schedule` is an IData document containing the details of the
  scheduled task identified by the given `$id`, if it exists.
  * `id` is the identifying string for this scheduled task.
  * `type` is the type of schedule, a choice of one of the following:
    * `once` is a schedule that executes one time only at the specified
      start datetime.
    * `repeat` is a schedule that executes periodically at the specified
      `repeat/interval` of time.
    * `complex` is a schedule that executes periodically on the given
      minutes, hours, week days, days of month, and months of the year.
  * `service` is the fully-qualified name of the service executed by
    the schedule.
  * `package` is the name of the package the scheduled `service` is a
    member of.
  * `description` is an optional description of the scheduled task.
  * `target` identifies the server or servers the schedule executes
    the service on, provided either as a specific `hostname:port`, or
    one of the following values:
    * `$any` executes the task on any server in the cluster. This
      option ensures the service is only executed once at any one
      time, but can be executed by any server in the cluster.
    * `$all` executes the task on all servers in the cluster. This
      option executes the service on every cluster node at the
      same time to perform, for example, clean up or house keeping
      on every server.
  * `user` is the user account under which the service is executed.
  * `start` is the optional datetime from which the scheduled task
    is in effect or active. For `once` only tasks, this is the
    datetime the service will be executed once and only once.
  * `end` is the optional datetime after which the scheduled task
    is no longer be in effect (expires). If not specified, the
    schedule never expires.
  * `overlap?` is an boolean determining how to handle when one
    execution of the scheduled task overlaps the next scheduled
    execution, for example when the execution duration exceeds the
    schedule interval.

    When true, the next scheduled execution of the service
    executes as per the schedule, even if the previous schedule is
    still executing, which can result in multiple concurrent
    executions of the service.

    When false, the schedule is never executed concurrently.
    For a repeat schedule, the next scheduled time is calculated
    from the end of execution of the previous schedule. For a complex
    schedule, any scheduled times that occur while the previous
    schedule is executing are skipped, and the service will execute
    at the next uncontested scheduled time.
  * `lateness` is an optional IData document containing arguments for
    how to determine when, and then handle if, a schedule is late.
    * `duration` is the duration of time which when lapsed passed the
      scheduled time determines that the task is considered late.
    * `action` determines what happens to the task when its determined
      to be late, and is a choice of one of the following:
      * `run immediately`
      * `skip and run at next scheduled interval`
      * `suspend`
  * `repeat` is an optional IData document containing arguments only
    applicable when the schedule type is `repeat`.
    * `interval` is the duration of time that determines how often
      the scheduled task will execute.
  * `complex` is an optional IData document containing arguments only
    applicable when the schedule type is `complex`.
    * `months` is an optional list of months of the year, provided as
      an integer between 1 (January) and 12 (December), the schedule
      executes in. If not specified, the schedule executes in all
      months of the year.
    * `days` is an optional list of days of the month, provided as an
      integer between 1 and 31, the schedule executes on. If not
      specified, the schedule executes on all days in the month.
    * `weekdays` is an optional list of days of the week, provided as
      an integer between 1 (Sunday) and 7 (Saturday), the schedule
      executes on. If not specified, the schedule executes on all
      days of the week.
    * `hours` is an optional list of hours of the day, provided as an
      integer between 0 and 23, the schedule executes on. If not
      specified, the schedule executes every hour.
    * `minutes` is an optional list of minutes of the hour, provided as
      an integer between 0 and 59, the schedule executes on. If
      not specified, the schedule executes every minute.
  * `pipeline` is an optional IData document containing the input
    arguments used as the input pipeline when executing the service.
  * `status` is the current status of the scheduled task, and is a choice
    of one of the following:
    * `cancelled`
    * `running`
    * `suspended`
    * `waiting`
  * `next` is the datetime of the next scheduled execution of the
    service, if applicable.

---

### tundra.schedule:list

Returns a list of all scheduled tasks that satisfy the given `$filter`
condition, or every task if no `$filter` is specified.

#### Inputs:

* `$filter` is an optional `tundra.condition:evaluate` conditional
  statement, used to filter the list of scheduled tasks returned. The
  conditional statement is evaluated against the pipeline, which
  includes the task being evaluated as an IData document named
  `$schedule` with the same structure as returned by
  `tundra.schedule:get`.

  Examples of some filter strings are as follows:

  1. Return a list of only the complex scheduled tasks:

     `%$schedule/type%=="complex"`

  2. Return a list of only the scheduled tasks that execute
     `pub.string:concat`:

     `%$schedule/service%=="pub.string:concat"`

  3. Return a list of only the scheduled tasks that are suspended for
     a service whose name is stored in the pipeline variable `svc`:

     `%$schedule/status%=="suspended" and %$schedule/service%==%svc%`

  Refer to the `tundra.condition:evaluate` service documentation for the
  format of conditional statements.

#### Outputs:

* `$schedules` is a document list (IData[]) containing the details of all
  the scheduled tasks that satisfy the given `$filter`, or every
  scheduled task known to the task scheduler on this Integration Server
  if no `$filter` is specified.
  * `id` is the identifying string for this scheduled task.
  * `type` is the type of schedule, a choice of one of the following:
    * `once` is a schedule that executes one time only at the specified
      start datetime.
    * `repeat` is a schedule that executes periodically at the specified
      `repeat/interval` of time.
    * `complex` is a schedule that executes periodically on the given
      minutes, hours, week days, days of month, and months of the year.
  * `service` is the fully-qualified name of the service executed by
    the schedule.
  * `package` is the name of the package the scheduled `service` is a
    member of.
  * `description` is an optional description of the scheduled task.
  * `target` identifies the server or servers the schedule executes
    the service on, provided either as a specific hostname:port, or
    one of the following values:
    * `$any` executes the task on any server in the cluster. This
      option ensures the service is only executed once at any one
      time, but can be executed by any server in the cluster.
    * `$all` executes the task on all servers in the cluster. This
      option executes the service on every cluster node at the
      same time to perform, for example, clean up or house keeping
      on every server.
  * `user` is the user account under which the service is executed.
  * `start` is the optional datetime from which the scheduled task
    is in effect or active. For `once` only tasks, this is the
    datetime the service will be executed once and only once.
  * `end` is the optional datetime after which the scheduled task
    is no longer be in effect (expires). If not specified, the
    schedule never expires.
  * `overlap?` is an boolean determining how to handle when one
    execution of the scheduled task overlaps the next scheduled
    execution, for example when the execution duration exceeds the
    schedule interval.

    When true, the next scheduled execution of the service
    executes as per the schedule, even if the previous schedule is
    still executing, which can result in multiple concurrent
    executions of the service.

    When false, the schedule is never executed concurrently.
    For a repeat schedule, the next scheduled time is calculated
    from the end of execution of the previous schedule. For a complex
    schedule, any scheduled times that occur while the previous
    schedule is executing are skipped, and the service will execute
    at the next uncontested scheduled time.
  * `lateness` is an optional IData document containing arguments for
    how to determine when, and then handle if, a schedule is late.
    * `duration` is the duration of time which when lapsed passed the
      scheduled time determines that the task is considered late.
    * `action` determines what happens to the task when its determined
      to be late, and is a choice of one of the following:
      * `run immediately`
      * `skip and run at next scheduled interval`
      * `suspend`
  * `repeat` is an optional IData document containing arguments only
    applicable when the schedule type is `repeat`.
    * `interval` is the duration of time that determines how often
      the scheduled task will execute.
  * `complex` is an optional IData document containing arguments only
    applicable when the schedule type is `complex`.
    * `months` is an optional list of months of the year, provided as
      an integer between 1 (January) and 12 (December), the schedule
      executes in. If not specified, the schedule executes in all
      months of the year.
    * `days` is an optional list of days of the month, provided as an
      integer between 1 and 31, the schedule executes on. If not
      specified, the schedule executes on all days in the month.
    * `weekdays` is an optional list of days of the week, provided as
      an integer between 1 (Sunday) and 7 (Saturday), the schedule
      executes on. If not specified, the schedule executes on all
      days of the week.
    * `hours` is an optional list of hours of the day, provided as an
      integer between 0 and 23, the schedule executes on. If not
      specified, the schedule executes every hour.
    * `minutes` is an optional list of minutes of the hour, provided as
      an integer between 0 and 59, the schedule executes on. If
      not specified, the schedule executes every minute.
  * `pipeline` is an optional IData document containing the input
    arguments used as the input pipeline when executing the service.
  * `status` is the current status of the scheduled task, and is a choice
    of one of the following:
    * `cancelled`
    * `running`
    * `suspended`
    * `waiting`
  * `next` is the datetime of the next scheduled execution of the
    service, if applicable.

---

### tundra.schedule:remove

Deletes (or cancels) either the scheduled task identified by the given
`$id`, or every scheduled task that satisfies the given `$filter`
condition.

The `$id` and `$filter` input arguments are mutually exclusive: only one
should be specified.

#### Inputs:

* `$id` is an optional string identifier which identifies the schedule
  task to be deleted.
* `$filter` is an optional `tundra.condition:evaluate` conditional
  statement. All scheduled tasks which satisfy the given filter
  condition will be deleted. The conditional statement is evaluated
  against the pipeline, which includes the task being evaluated as an
  IData document named `$schedule` with the same structure as returned
  by `tundra.schedule:get`.

  Examples of some filter strings are as follows:

  1. Delete all complex scheduled tasks:

     `%$schedule/type% == "complex"`

  2. Delete all scheduled tasks that execute `pub.string:concat`:

     `%$schedule/service% == "pub.string:concat"`

  3. Delete only the scheduled tasks that are suspended for a service
     whose name is stored in the pipeline variable `svc`:

     `%$schedule/status% == "suspended" and %$schedule/service% == %svc%`

  Refer to the `tundra.condition:evaluate` service documentation for the
  format of conditional statements.

---

### tundra.schedule:resume

Resumes (or unpauses) either the scheduled task identified by the given
`$id`, or every scheduled task that satisfies the given `$filter`
condition.

Suspended tasks do not execute for the period they are suspended.
Previously suspended tasks that are resumed will begin executing again
according to their schedule.

Attempting to resume tasks which are not suspended has no effect on the
task, and results in no exception being thrown.

The `$id` and `$filter` inputs are mutually exclusive: only one should be
specified.

#### Inputs:

* `$id` is an optional string identifier which identifies the schedule
  task to be resumed.
* `$filter` is an optional `tundra.condition:evaluate` conditional
  statement. All scheduled tasks which satisfy the given filter
  condition will be resumed. The conditional statement is evaluated
  against the pipeline, which includes the task being evaluated as an
  IData document named `$schedule` with the same structure as returned
  by `tundra.schedule:get`.

  Examples of some filter strings are as follows:

  1. Resume all complex scheduled tasks:

     `%$schedule/type% == "complex"`

  2. Resume all scheduled tasks that execute `pub.string:concat`:

     `%$schedule/service% == "pub.string:concat"`

  3. Resume only the scheduled tasks that are suspended for a service
     whose name is stored in the pipeline variable `svc`:

     `%$schedule/status% == "suspended" and %$schedule/service% == %svc%`

  Refer to the `tundra.condition:evaluate` service documentation for the
  format of conditional statements.

---

### tundra.schedule:suspend

Suspends (or pauses) either the scheduled task identified by the given
`$id`, or every scheduled task that satisfies the given `$filter`
condition.

Suspended tasks do not execute for the period they are suspended. To start
executing a suspended task according to its schedule, it should be resumed
using `tundra.schedule:resume`.

Attempting to suspend tasks that are already suspended has no effect on the
task, and results in no exception being thrown.

The `$id` and `$filter` inputs are mutually exclusive: only one should be
specified.

#### Inputs:

* `$id` is an optional string identifier which identifies the schedule
  task to be suspend.
* `$filter` is an optional `tundra.condition:evaluate` conditional
  statement. All scheduled tasks which satisfy the given filter
  condition will be suspended. The conditional statement is evaluated
  against the pipeline, which includes the task being evaluated as an
  IData document named `$schedule` with the same structure as returned
  by `tundra.schedule:get`.

  Examples of some filter strings are as follows:

  1. Suspend all complex scheduled tasks:

     `%$schedule/type% == "complex"`

  2. Suspend all scheduled tasks that execute `pub.string:concat`:

     `%$schedule/service% == "pub.string:concat"`

  3. Suspend only the scheduled tasks that are not already suspended
     for a service whose name is stored in the pipeline variable `svc`:

     `%$schedule/status% != "suspended" and %$schedule/service% == %svc%`

  Refer to the `tundra.condition:evaluate` service documentation for the
  format of conditional statements.

---

### tundra.schema.content.deliver:handler

Content delivery protocol handling services used by tundra.content:deliver
must implement this specification.

#### Inputs:

* `$content` is a string, byte array, or input stream containing data to
  be delivered to the `$destination` URI.
* `$content.type` is an optional MIME media type describing the type
  content being delivered.
* `$encoding` is an optional character set to use when `$content` is
  provided as a string to encode the text data upon delivery. Defaults
  to [UTF-8].
* `$destination` is a parsed URI identifying the location where the given
  $content should be delivered.

#### Outputs:

* `$message` is an optional response message, useful for logging, that
  may be returned by specific delivery protocols.
* `$response` is an optional response content returned by the delivery
  (for example, the HTTP response body).
* `$response.type` is an optional MIME media type describing the type of
  `$response` returned.

---

### tundra.schema.content.retrieve:handler

Content retrieval protocol handling services used by
`tundra.content:retrieve` must implement this specification.

#### Inputs:

* `$source` is the source URI identifying the location and names of the
  content to be retrieved.
* `$service` is the content processing service that is called for each
  item of content retrieved from `$source`. This service is required to
  implement the `tundra.schema.content.retrieve:processor` specification.
* `$limit` is an optional maximum number of content items to be
  retrieved from `$source` per retrieval.

#### Outputs:

* `$message` is an optional diagnostic message describing the results of
  the retrieval.

---

### tundra.schema.content.retrieve:processor

Content processing services used by `tundra.content:retrieve` must
implement this specification.

#### Inputs:

* `$content` is the content to be processed, specified as a
  [java.io.InputStream].
* `$content.type` is the mime media type of the content to be processed.
* `$content.name` is the name associated with the content, such as a
  file name.

---

### tundra.schema.exception:handler

Exception handling `$catch` services called by `tundra.service:ensure` can
implement this specification.

#### Inputs:

* `$exception` is the [java.lang.Throwable] object that was caught by
  this handler to be handled.
* `$exception?` is a boolean flag indicating if an exception was thrown.
* `$exception.class` is the Java class name of the caught exception
  object.
* `$exception.message` is the exception message describing the error
  that has occurred.
* `$exception.service` is the fully qualified name of the service that threw
  the exception.
* `$exception.package` is the package that `$exception.service` resides in,
  specified only if `$exception.service` exists.
* `$exception.info` is the exception information returned by
  `WmPublic/pub.flow:getLastError`.
* `$exception.stack` is the Java call stack describing where the error
  occurred.

---

### tundra.schema.http.response:handler

Specifies the required inputs and outputs for an [HTTP] response handling
service called by `tundra.http:client`.

#### Inputs:

* `$response` is the [HTTP] response to be processed by this service.

#### Outputs:

* `$response` is the processed [HTTP] response. How the response is
  processed is at the discretion of the implementing service. Refer to
  the standard `tundra.http.response:handle` service for a reference
  implementation.

---

### tundra.service:benchmark

Benchmarks the performance of the given service by invoking it the
given number of times, and returning the average and standard
deviation execution durations.

#### Inputs:

* `$service` is the fully-qualified name of the service to be
  benchmarked.
* `$pipeline` is an optional IData document which, if specified,
  contains the input arguments for the invocation of `$service`; in
  other words, the invocation is scoped to this IData document. If
  not specified, the invocation is unscoped, and hence the service
  will operate directly against the pipeline itself.
* `$count` is the number of times `$service` will be invoked, and must
  be greater than or equal to 1. The more times `$service` is
  invoked, the more reliable the resulting statistics will be (in
  other words, the more samples the better).

#### Outputs:

* `$duration.average` is the calculated mean or average duration of
  execution of the given service with the given input pipeline
  formatted as an [ISO8601] XML duration string.
* `$duration.standard.deviation` is the calculated standard
  deviation duration of execution of the given service with the
  given input pipeline formatted as an [ISO8601] XML duration
  string.
* `$duration.minimum` is the minimum duration of execution of the
  given service with the given input pipeline formatted as an
  [ISO8601] XML duration string.
* `$duration.maximum` is the maximum duration of execution of the
  given service with the given input pipeline formatted as an
  [ISO8601] XML duration string.
* `$duration.total` is the total duration of all executions of the
  given service with the given input pipeline formatted as an
  [ISO8601] XML duration string.
* `$message` is a diagnostic message describing the execution
  statistics of the benchmarked service, which can be used for
  logging the results of the benchmark:

      tundra.service:sleep benchmark results: average = 250.460 ms, standard deviation = 1.711 ms, minimum = 250.000 ms, maximum = 277.000 ms, total = 25046.000 ms, sample count = 100

---

### tundra.service:callstack

Returns the current call stack.


#### Outputs:

* `$callstack` is a list of fully-qualified service names
  in invocation order with the top-level service as the
  first item.
* `$callers` is a string representation of the call stack,
  which is useful for diagnostics/logging.
* `$caller` is the fully-qualified service name of the
  immediate caller of the service which invoked
  tundra.service:callstack, which is useful for obtaining
  the name of the service which called the currently
  executing service.

---

### tundra.service:create

Creates a new empty flow service with the given name in an existing
package.

#### Inputs:

* `$package` is the name of the existing package in which to create
  a new empty flow service. If the package does not exist, an
  exception will be thrown.
* `$service` is the fully-qualified name of the service to be
  created, such as `folder1.folder2:service3`. If an element with
  the given name already exists, an exception will be thrown.

---

### tundra.service:defer

Defers execution of the given service using a dedicated single thread. The
service is not executed immediately, and is instead queued to wait to be
executed until the defer thread becomes available.

* Inputs
* `$service` is the fully-qualified name of the service to be deferred.
* `$pipeline` is an optional IData document which, if specified, contains
  the input arguments for the invocation of `$service`; in other words, the
  invocation is scoped to this IData document. If not specified, the
  invocation is unscoped, and hence the service will operate directly
  against the pipeline itself.

---

### tundra.service:ensure

Provides a try/catch/finally pattern for flow services.

#### Inputs:

* `$service` is the fully-qualified name of a service invoked
  in the [try block].
* `$catch` is an optional fully-qualified name of a service
  invoked in the [catch block]. This service should
  implement the `Tundra/tundra.schema.exception:handler`
  specification. If no `$catch` service is specified, the
  exception will be rethrown.
* `$finally` is an optional fully-qualified name of a service
  invoked in the [finally block] (always invoked regardless
  of whether an exception is thrown by `$service` or not).
* `$pipeline` is an optional IData document which, if specified,
  contains the input arguments for the invocations of `$service`,
  `$catch`, and `$finally`; in other words, the calls to these
  services are scoped to this IData document. If not specified,
  the invocations are not scoped, and hence these services
  operate directly against the pipeline itself.

#### Outputs:

* `$pipeline` is the output pipeline of the invocations of
  `$service`, `$catch`, and `$finally`. This is only returned if
  `$pipeline` was specified as an input. Otherwise, the outputs
  of these invocations are merged directly with the pipeline
  itself.

---

### tundra.service:initiator

Returns true if the calling service is the initiating top-level
service for this thread.


#### Outputs:

* `$initiator?` is a boolean indicating if the calling service
  is the top-level service that initiated this thread.

---

### tundra.service:invoke

Invokes a service either synchronously or asynchronously, and
either scoped or unscoped.

* Inputs
* `$service` is the fully-qualified name of the service to be
  invoked.
* `$pipeline` is an optional IData document which, if specified,
  contains the input arguments for the invocation of `$service`;
  in other words, the invocation is scoped to this IData document.
  If not specified, the invocation is unscoped, and hence the
  service will operate directly against the pipeline itself.
* `$mode` is an optional choice of execution mode: either synchronous
  or asynchronous. When synchronous, the invocation of
  `Tundra/tundra.service:invoke` blocks until the invocation of
  `$service` completes. When asynchronous, `Tundra/tundra.service:invoke`
  returns immediately and the invocation of `$service` occurs on another
  thread.

#### Outputs:

* `$pipeline` is the output pipeline of the invocation of
  `$service`. This is only returned if the `$mode` is synchronous,
  and the invocation was scoped (`$pipeline` was specified as an
  input). If the invocation was synchronous and unscoped, the
  outputs of the invocation are merged directly with the pipeline
  itself. If the invocation was asynchronous, then no outputs
  are returned.
* `$thread` is returned only when the invocation `$mode` is asynchronous,
  and is the thread object which can later be waited on to finish
  using `Tundra/tundra.service:join`.

---

### tundra.service:join

Waits for the given service thread to finish before returning the
service output pipeline.

* Inputs
* `$thread` is a service thread object returned by
  `Tundra/tundra.service:invoke` when a service is invoked
  asynchronously.

#### Outputs:

* `$pipeline` is the output pipeline returned by the service
  thread.

---

### tundra.service:nothing

This service deliberately does nothing.

---

### tundra.service:reflect

Returns information about the service with the given name.

#### Inputs:

* `$service` is the name of the service to be reflected on.

#### Outputs:

* `$service.properties`
  * `name` is the name of the service.
  * `type` is the type of service, such as flow or java.
  * `package` is the name of the package the service exists in.
  * `description` is the service comments.

---

### tundra.service:respond

Forces the specified response status and body to be returned by webMethods
Integration Server to the calling process (such as an HTTP or FTP client).
This service only works correctly when invoked by the top-level initiating
service of the current thread, an unfortunate limitation of the platform.

Unlike WmPublic/pub.flow:setResponseCode, which throws an exception when
invoked for transports other than HTTP, this service succeeds regardless
of the invoking transport.

#### Inputs:

* `$code` is the [HTTP status code] for the returned response. For
  example, a 200 status code indicates the request was successful.
  For transports other than HTTP, a status code >= 400 will result
  in an exception being thrown. This is the appropriate response,
  as it will result in the correct action for those transports
  occurring (a SOAP fault for a SOAP invocation, a file transfer
  failure for an FTP invocation, etc).
* `$message` is an optional message to be associated with the given
  status code. If not specified, the standard status message for
  the code will be used. This is only applicable to HTTP transports.
* `$headers` is an optional IData document containing HTTP header
  keys and values to be added to the response. This is only applicable
  to HTTP transports.
* `$content` is an optional string, byte array, or input stream
  containing the response body to be returned. If not specified,
  defaults to an empty string.
* `$content.type` is the mime type of the given response body content. If
  not specified, defaults to application/octet-stream (the mime type for
  arbitrary binary data).
* `$encoding` is an optional character set used to encode $content when
  specified as a string. Defaults to [UTF-8].

---

### tundra.service:self

Returns the name of the current service, or nothing if invoked
directly.


#### Outputs:

* `$self` is the fully-qualified name of the current service
  (the service which called this service), or nothing if invoked
  directly.

---

### tundra.service:sleep

Sends the currently executing thread to sleep (temporarily
cease execution) for the specified duration, subject to the
precision and accuracy of system timers and schedulers.

#### Inputs:

* `$duration` is an [ISO8601] XML duration for which the
  current thread should sleep.

---

### tundra.service:validate

Returns true if the given service name exists and is actually
a service.

#### Inputs:

* `$service` is the name to be validated.
* `$raise?` is a boolean flag indicating if an exception should
  be thrown if the given service is not valid. Defaults to
  false.

#### Outputs:

* `$valid?` is a boolean flag indicating if the given service
  exists and is actually a service on this Integration Server.

---

### tundra.session:get

Returns information about the current session, including values stored in
session state.


#### Outputs:

* `$session` is an IData document containing information about the
  current session.

---

### tundra.session:put

Stores the given key value pair in current session state.

#### Inputs:

* `$key` is the optional key name to use to identify the value
  to be stored in session state. If not specified, this
  service does nothing.
* `$value` is the optional value to be stored in session state.

---

### tundra.session:remove

Removes the given key value pair from current session state.

#### Inputs:

* `$key` is the optional key name to use to identify the key
  value pair to be removed from session state. If not specified,
  this service does nothing, otherwise the key value pair is
  removed from session state.

#### Outputs:

* `$value` was the value associated with the removed `$key`.

---

### tundra.stream:close

Closes the given input stream or output stream, and releases any associated
system resources.

#### Inputs:

* `$stream` is an optional [java.io.InputStream] or
  [java.io.OutputStream] object to be closed. If specified, the stream
  is closed and any associated system resources are released. If not
  specified, this service does nothing.

---

### tundra.stream:copy

Copies all data from the given input stream (or string or bytes) to the
given output stream, then closes the streams.

#### Inputs:

* `$input` is an optional String, byte[], or [java.io.InputStream] object
  containing data to be written to `$output`. If not specified, this
  service does nothing.
* `$output` is an optional [java.io.OutputStream] object where data read
  from `$input` is to be written. If not specified, this service does
  nothing.
* `$close?` is an optional boolean indicating whether the streams should be
  closed after the copy is complete. Defaults to `true`.

---

### tundra.stream:normalize

Converts a string, bytes or input stream to an input stream.

#### Inputs:

* `$object` is an optional [java.lang.String], byte[], or
  [java.io.InputStream] object to be converted to a
  [java.io.InputStream] object. If not specified, this service does
  nothing.
* `$encoding` is the character set used to encode the character data
  when `$object` is specified as a [java.lang.String]. Defaults to
  [UTF-8].

#### Outputs:

* `$stream` is an optional [java.io.InputStream] object from which can
  be read the data represented by `$object`. If `$object` was not
  specified, no `$stream` is returned.

---

### tundra.string.base64:decode

[Base64] decodes the given string.

#### Inputs:

* `$base64` is a [Base64] encoded string to be decoded.

#### Outputs:

* `$string` is the [Base64] decoded data as a string.

---

### tundra.string.base64:encode

[Base64] encodes the given string.

#### Inputs:

* `$string` is a string containing data to be [Base64] encoded.

#### Outputs:

* `$base64` is the [Base64] encoded data as a string.

---

### tundra.string:blankify

Returns an empty string if the given input string is null, otherwise
returns the given input string unchanged.

#### Inputs:

* `$string` is an optional string input value.

#### Outputs:

* `$string` is an empty string if the input `$string` was null,
  otherwise `$string` is returned unchanged.

---

### tundra.string:capitalize

Capitalizes the first character in either the first word or
all words in the given string.

#### Inputs:

* `$string` is the string to be capitalized.
* `$mode `is an optional choice that determines whether
  all words or only the first word is capitalized:
  * `all words`
  * `first word`

  Defaults to `all words`, if not specified.

#### Outputs:

* `$string` is the given string capitalized.

---

### tundra.string:capture

Returns the associated [capturing groups] if the given [regular expression
pattern] is found anywhere in the given string.

Refer to the following excerpt from the [java.util.regex.Pattern]
documentation:

> Groups and capturing
>
> Capturing groups are numbered by counting their opening parentheses from
> left to right. In the expression `((A)(B(C)))`, for example, there are four
> such groups:
>
> 1. `((A)(B(C)))`
> 2. `(A)`
> 3. `(B(C))`
> 4. `(C)`
>
> Group zero always stands for the entire expression.
>
> Capturing groups are so named because, during a match, each subsequence of
> the input sequence that matches such a group is saved. The captured
> subsequence may be used later in the expression, via a back reference, and
> may also be retrieved from the matcher once the match operation is
> complete.
>
> The captured input associated with a group is always the subsequence that
> the group most recently matched. If a group is evaluated a second time
> because of quantification then its previously-captured value, if any, will
> be retained if the second evaluation fails. Matching the string "aba"
> against the expression `(a(b)?)+`, for example, leaves group two set to "b".
> All captured input is discarded at the beginning of each match.
>
> Groups beginning with `(?` are pure, non-capturing groups that do not
> capture text and do not count towards the group total.

#### Inputs:

* `$string` is a string to be searched for the given pattern.
* `$pattern` is the [regular expression pattern] to search for against the
  given string.

#### Outputs:

* `$captured?` is a boolean which when true indicates that the [regular
  expression pattern] was found at least once in the given `$string`.
* `$captures` is an `IData[]` document list where each item contains the
  [capturing groups] associated with each time `$pattern` was found in
  `$string`.
  * `groups` is an `IData[]` document list where each item in the list
    represents a single [capturing group]. Note that the first (zeroth)
    item in the list always denotes the entire pattern.
    * `captured?` is a boolean flag which when true indicates that this
      [capturing group] was captured in the matched substring. This flag
      will be false for optional [capturing groups] that are not found in
      the matched substring.
    * `index` is the zero-based start index of the captured substring for
      this [capturing group], and is only specified when `$captured?` is
      true.
    * `length` is the number of captured characters in the captured
      substring for this [capturing group], and is only specified when
      `$captured?` is true.
    * `content` is captured substring for this [capturing group], and is
      only specified when `$captured?` is true.
  * `groups.length` is the number of items in the `groups` IData[] document
    list, which is equal to the number of [capturing groups] + 1 (where
    the additional item is the entire pattern) present in the [regular
    expression pattern].
* `$captures.length` is both the number of times the [regular expression
  pattern] was found in the given `$string`, and the number of items in
  the `$captures` `IData[]` document list.

---

### tundra.string:characters

Returns the given string as a list of characters.

#### Inputs:

* `$string` is a string to be split into characters.

#### Outputs:

* `$characters` is a list of the characters in the
  given `$string`.

---

### tundra.string:coalesce

Returns the first string argument that is not null.

#### Inputs:

* `$string.x` is an optional string argument.
* `$string.y` is an optional string argument.
* `$mode` determines what is returned when all
  arguments are null:
  * `missing`: `$string` is not returned when all
    arguments are null. This is the default, if
    `$mode` is not specified.
  * `null`: `$string` is returned as null when all
    arguments are null.

#### Outputs:

* `$string` is the first of the given arguments
  whose value is not null, or null if all
  arguments were null and `$mode` is 'null'.

---

### tundra.string:compare

Compares two strings indicating their lexicographical position
relative to one another.

Implemented using the java.lang.String [compareTo] and
[compareToIgnoreCase] methods.

#### Inputs:

* `$string.x` is the datetime string to be compared to `$string.y`.
* `$string.y` is the datetime string to be compared to `$string.x`.
* `$case.insensitive?` is a boolean flag indicating if the string
  comparison should ignore case differences. Defaults to false.

#### Outputs:

* `$before?` is a boolean flag indicating if `$string.x` is less than
  `$string.y`.
* `$equal?` is a boolean flag indicating if `$string.x` and `$string.y`
  are equal.
* `$after?` is a boolean flag indicating if `$string.x` is greater
  than `$string.y`.

[compareTo]: <http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#compareTo(java.lang.String)>
[compareToIgnoreCase]: <http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#compareToIgnoreCase(java.lang.String)>

---

### tundra.string:find

Returns whether the given [regular expression pattern] or literal
string is found anywhere in the given string.

#### Inputs:

* `$string` is a string to be searched for the given pattern.
* `$pattern` is the [regular expression pattern] or literal string
  to search for against the given string.
* `$literal?` is a boolean indicating if the `$pattern` string should
  be treated as a literal string. If false, `$pattern` is treated
  as a [regular expression pattern]. If true, `$pattern` is treated
  as a literal string. Defaults to false, if not specified.

#### Outputs:

* `$found?` is true if the given pattern is found anywhere in the
  given string.

---

### tundra.string:length

Returns the number of characters in the given string.

#### Inputs:

* `$string` is an optional string to return the length of.

#### Outputs:

* `$length` is the number of characters in the given string,
  or zero if no string was specified.

---

### tundra.string:lines

Returns all the lines in the given string as a list.

#### Inputs:

* `$string` is an optional string to split into lines.

#### Outputs:

* `$lines` is a string list containing each line from
  the given string.

---

### tundra.string:listify

Converts the value or values identified by the given key to a new
list containing the original value or values as its items, unless
the original value is already a list in which case it remains
unmodified.

#### Inputs:

* `$key` is a simple or fully-qualified (such as `a/b/c[0]/d`) key
  identifying the value to be converted to a list.
* `$scope` is an optional `IData` document that, if specified, is used
  to resolve the given `$key` against. If not specified, `$key` is
  resolved against the pipeline.

#### Outputs:

* `$scope` is returned if an input `$scope` was provided, where the value
  identified by `$key` within it has been converted to a list. If
  no input `$scope` was specified, the value identified by `$key` in the
  pipeline is instead converted to a list. If `$key` does not identify
  any value, this service does nothing.

---

### tundra.string:lowercase

Returns the given string in lower case.

#### Inputs:

* `$string` is a string to be converted to lower case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.

#### Outputs:

* `$string` is a lower case version of the input string, where
  all characters have been converted to their lower case
  equivalent according to the transformation rules of the given
  [Locale].

---

### tundra.string:match

Returns whether the given [regular expression pattern] or literal
string matches the given string.

#### Inputs:

* `$string` is a string to be matched against the given pattern.
* `$pattern` is the [regular expression pattern] or literal string
  to match against the given string.
* `$literal?` is a boolean indicating if the `$pattern` string should
  be treated as a literal string. If false, `$pattern` is treated
  as a [regular expression pattern]. If true, `$pattern` is treated
  as a literal string. Defaults to false, if not specified.

#### Outputs:

* `$match?` is true if the given string matches the given pattern.

---

### tundra.string:normalize

Converts a string, bytes or input stream to a string.

#### Inputs:

* `$object` is a string, byte array, or input stream to be
  converted to a string.
* `$encoding` is an optional character set to use when `$object`
  is a byte array or input stream. Defaults to [UTF-8].

#### Outputs:

* `$string` is the given object converted to a string.

---

### tundra.string:pad

Pads the given string with the given character as many times as necessary
to reach the given length.

#### Inputs:

* `$string` is the string to be padded to the given length. If
  `$string.length >= |$length|`, `$string` is returned unmodified.
* `$length` is the minimum desired length for the returned string. If
  specified as a positive integer, `$string` will be padded from the
  left by prepending it with `(|$length|- $string.length)` characters.
  If specified as a negative integer, $string will be padded from the
  right by appending it with `(|$length|- $string.length)` characters.
* `$character` is the character to use when padding `$string`. If
  `$character.length` > 1, only the first character in `$character` will
  be used. Defaults to ' ' (space character), if not specified.

#### Outputs:

* `$string` is the resulting padded string.

---

### tundra.string:quote

Returns a [regular expression pattern] that can be used to match the
given string literally. Regular expression metacharacters or escape
sequences in the input sequence are given no special meaning.

#### Inputs:

* `$string` is a string value to be converted to a
  [regular expression pattern].

#### Outputs:

* `$pattern` is a [regular expression pattern] that can be used to match
  the given `$string` literally.

---

### tundra.string:replace

Replaces all occurrences of the given [regular expression pattern]
in the given string, with the replacement string.

#### Inputs:

* `$string` is a string to have all occurrences of the given
  [regular expression pattern] replaced.
* `$pattern` is the [regular expression pattern] to match against
  the given string.
* `$replacement` is the replacement string to be substituted in
  the given string wherever the given pattern is found. If not
  specified, no replacement will occur.
* `$literal?` is a boolean indicating if the replacement string
  should be treated as a literal string. If false, captured
  groups can be referred to with dollar-sign references, such
  as $1, and other special characters may need to be escaped.
  Defaults to false.

#### Outputs:

* `$string` is the input string with all occurrences of the given
  [regular expression pattern] replaced with `$replacement`.

---

### tundra.string:slice

Returns a new string which is a subset of the characters in the given
string.

Examples:

    slice($string = "hamburger", $index =  0, $length =  3) == "ham"
    slice($string = "hamburger", $index =  2, $length = -3) == "ham"
    slice($string = "hamburger", $index = -7, $length = -3) == "ham"
    slice($string = "hamburger", $index =  4, $length =  4) == "urge"
    slice($string = "hamburger", $index =  7, $length = -4) == "urge"
    slice($string = "hamburger", $index = -2, $length = -4) == "urge"

#### Inputs:

* `$string` is the string to be sliced.
* `$index` is an optional zero-based index from which to take the slice.
  Supports forward and reverse indexing where a positive index is a
  normal zero-based array index from left to right, and a negative index
  is a reverse array index from right to left (for example, an index of
  -1 is the last item in the list, and an index of -2 is the second last
  item in the list). If not specified, defaults to 0.
* `$length` is the number of characters to include in the slice. Supports
  positive and negative lengths where a positive length will slice from
  left to right, and a negative length will slice from right to left.
  If not specified, a left to right slice containing all remaining
  characters after `$index` will be returned when `$index` is positive,
  or a right to left slice containing all remaining characters before
  `$index` will be returned when `$index` is negative.

#### Outputs:

* `$string` is the desired subset or slice of the given string.

---

### tundra.string:split

Splits the given string around matches of the given literal pattern or
[regular expression pattern].

#### Inputs:

* `$string` is a string to be split into tokens using the given
  pattern as the token separator.
* `$pattern` is the literal or [regular expression pattern] to match
  against the given string.
* `$literal?` is a boolean indicating if the pattern should be
  treated as a literal string or a [regular expression pattern].
  Defaults to false.

#### Outputs:

* `$list` is the list of tokens that were found in the input string
  that were separated with an occurence of the given pattern.

---

### tundra.string:squeeze

Replaces runs of one or more whitespace characters (space, tab,
carriage return, line feed) with a single space character.

#### Inputs:

* `$string` is a string to squeeze extraneous whitespace from.

#### Outputs:

* `$string` is the input string with extraneous whitespace
  characters removed and replaced with a single space
  character.

---

### tundra.string:substitute

Attempts variable substitution on the given string by replacing all
occurrences of substrings matching "%key%" with the associated
(optionally scoped) value.

Optionally replaces null or non-existent values with the given default
value.

#### Inputs:

* `$string` is a string to perform variable substitution on.
* `$pipeline` is an optional IData document used to scope the
  variable substitution. If not specified, the substitution
  is unscoped (resolved against the pipeline itself).
* `$default` is an optional default value to substitute in place
  of null or missing values.

#### Outputs:

* `$string` is the input string with variable substitution patterns,
  such as "%key%", replaced with the value of the key (resolved
  against either $pipeline, if specified, or the pipeline itself).

---

### tundra.string:trim

Removes leading and trailing whitespace from the given string.

#### Inputs:

* `$string` is a string to be trimmed of leading and trailing
  whitespace characters.

#### Outputs:

* `$string` is the input string with leading and trailing whitespace
  characters removed.

---

### tundra.string:uppercase

Returns the given string in upper case.

#### Inputs:

* `$string` is a string to be converted to upper case.
* `$locale` optionally identifies the case transformation rules
  to be used for a given [Locale]. If not specified, the
  [default locale] is used.

#### Outputs:

* `$string` is an upper case version of the input string, where
  all characters have been converted to their upper case
  equivalent according to the transformation rules of the given
  [Locale].

---

### tundra.system:reflect

Returns information about Integration Server, such as the software
version, environment settings, Java properties, well-known directory
locations, and memory usage.


#### Outputs:

* `$system` is a returned `IData` document containing the software
  version, environment settings, Java properties, well-known
  directory locations, and memory usage.
  * `version` is the Integration Server software version number for
    the Integration Server on which this service is executed.
  * `environment` is an `IData` document containing all the environment
    variables defined on the system on which this service is executed.
  * `properties` is an `IData` document containing all the Java
    configuration properties defined for the JVM process in which
    this service is executed.
  * `directories` is an `IData` document containing all the well-known
    directory locations defined for the Integration Server on
    which this service is executed.
  * `memory` is an `IData` document containing the memory usage of the
    JVM process in which this service is executed:
    * `used` is the number of bytes of memory allocated to the JVM
      heap that are in use.
    * `free` is the number of bytes of memory allocated to the JVM
      heap that are not in use.
    * `total` is the total number of bytes of memory allocated to
      the JVM heap.

---

### tundra.thread:current

Returns information about the currently executing thread.


#### Outputs:

* `$thread` is an `IData` document containing information about the
  currently executing thread.
  * `id` is the unique identifier of the thread in the current execution
    context.
  * `name` is the human-readable name that was assigned to the thread
    when it was created.
  * `description` is the thread's built-in string representation of
    itself.
  * `state` is the thread's current status, and can be one of the
    following values:
    * `NEW` - a thread that has not yet started is in this state.
    * `RUNNABLE` - a thread executing in the Java virtual machine is in
      this state.
    * `BLOCKED` - a thread that is blocked waiting for a monitor lock is
      in this state.
    * `WAITING` - a thread that is waiting indefinitely for another
      thread to perform a particular action is in this state.
    * `TIMED_WAITING` - a thread that is waiting for another thread to
      perform an action for up to a specified waiting time is in this
      state.
    * `TERMINATED` - a thread that has exited is in this state.
  * `priority` is the thread's priority as an integer. Threads with
    higher priority are executed in preference to threads with lower
    priority.
  * `group` is the name of the thread group this thread belongs to.
  * `alive?` is a boolean indicating if the thread is currently alive. A
    thread is alive if it has been started and has not yet died.
  * `interrupted?` is a boolean indicating if the thread has been
    interrupted.
  * `daemon?` is a boolean indicating if the thread is a daemon thread.
    Daemon threads do not block the JVM from exiting even if they are
    still running.
  * `stack` is a document list describing the call stack associated with
    this thread.
  * `thread` is the actual `java.lang.Thread` object itself.

---

### tundra.thread:list

Returns a list of all threads known in the current execution context.


#### Outputs:

* `$threads` is an `IData[]` document list containing information about
  the all threads known in the current execution context.
  * `id` is the unique identifier of the thread in the current execution
    context.
  * `name` is the human-readable name that was assigned to the thread
    when it was created.
  * `description` is the thread's built-in string representation of
    itself.
  * `state` is the thread's current status, and can be one of the
    following values:
    * `NEW` - a thread that has not yet started is in this state.
    * `RUNNABLE` - a thread executing in the Java virtual machine is in
      this state.
    * `BLOCKED` - a thread that is blocked waiting for a monitor lock is
      in this state.
    * `WAITING` - a thread that is waiting indefinitely for another
      thread to perform a particular action is in this state.
    * `TIMED_WAITING` - a thread that is waiting for another thread to
      perform an action for up to a specified waiting time is in this
      state.
    * `TERMINATED` - a thread that has exited is in this state.
  * `priority` is the thread's priority as an integer. Threads with
    higher priority are executed in preference to threads with lower
    priority.
  * `group` is the name of the thread group this thread belongs to.
  * `alive?` is a boolean indicating if the thread is currently alive. A
    thread is alive if it has been started and has not yet died.
  * `interrupted?` is a boolean indicating if the thread has been
    interrupted.
  * `daemon?` is a boolean indicating if the thread is a daemon thread.
    Daemon threads do not block the JVM from exiting even if they are
    still running.
  * `stack` is a document list describing the call stack associated with
    this thread.
  * `thread` is the actual `java.lang.Thread` object itself.

---

### tundra.timezone:get

Returns the time zone associated with the given ID.

#### Inputs:

* `$id` is a [java.util.TimeZone] ID identifying the time
  zone to be returned, or a `(+|-)HH:mm` timezone offset,
  or an XML duration string representing a timezone offset,
  or a raw millisecond timezone offset, or `Z` for UTC, or
  `local` for the default localhost time zone. If a timezone
  offset is specified, the first timezone that matches the
  given offset is returned.
* `$datetime` is an optional XML datetime string identifying
  the instant in time to be used to determine the Universal
  Coordinated Time (UTC) offset and whether Daylight Savings
  Time (DST) is applicable. If not specified, defaults to
  the current datetime.
* `$datetime.pattern` is an optional datetime pattern that
  `$datetime` conforms to, and will be used to parse the
  datetime string. Defaults to an [ISO8601] XML datetime.

#### Outputs:

* `$timezone` is an IData document describing the time zone
  associated with the given `$id`.
  * `id` is the [java.util.TimeZone] ID associated with the
    time zone.
  * `name` is a short name associated with the time zone.
  * `description` is a long name associated with the time zone.
  * `utc.offset` is the historically correct XML duration added
    to UTC time to get local time in this time zone for the
    given `$datetime` instant. If daylight savings time is in
    effect for the given `$datetime` instant, then this value was
    adjusted to include the daylight savings offset.
  * `dst.used?` is a boolean indicating if daylight savings
    time is used by this time zone.
  * `dst.active?` is a boolean indicating if daylight savings
    time is in effect for the given `$datetime` instant.
  * `dst.offset` is the XML duration added to the UTC offset
    when daylight savings time is in effect.

---

### tundra.timezone:list

Returns all time zones known to the Java Virtual Machine.

#### Inputs:

* `$datetime` is an optional XML datetime string identifying
  the instant in time to be used to determine the Universal
  Coordinated Time (UTC) offset and whether Daylight Savings
  Time (DST) is applicable. If not specified, defaults to
  the current datetime.
* `$datetime.pattern` is an optional datetime pattern that
  `$datetime` conforms to, and will be used to parse the
  datetime string. Defaults to an [ISO8601] XML datetime.

#### Outputs:

* `$timezones` is an IData document list of time zones known to
  the running instance of the Java Virtual Machine.
  * `id` is the [java.util.TimeZone] ID associated with the
    time zone.
  * `name` is a short name associated with the time zone.
  * `description` is a long name associated with the time zone.
  * `utc.offset` is the historically correct XML duration added
    to UTC time to get local time in this time zone for the
    given `$datetime` instant. If daylight savings time is in
    effect for the given `$datetime` instant, then this value was
    adjusted to include the daylight savings offset.
  * `dst.used?` is a boolean indicating if daylight savings
    time is used by this time zone.
  * `dst.active?` is a boolean indicating if daylight savings
    time is in effect for the given `$datetime` instant.
  * `dst.offset` is the XML duration added to the UTC offset
    when daylight savings time is in effect.

---

### tundra.timezone:self

Returns the default time zone for this host.

#### Inputs:

* `$datetime` is an optional XML datetime string identifying
  the instant in time to be used to determine the Universal
  Coordinated Time (UTC) offset and whether Daylight Savings
  Time (DST) is applicable. If not specified, defaults to
  the current datetime.
* `$datetime.pattern` is an optional datetime pattern that
  `$datetime` conforms to, and will be used to parse the
  datetime string. Defaults to an [ISO8601] XML datetime.

#### Outputs:

* `$timezone` is an IData document describing the default time
  zone.
  * `id` is the [java.util.TimeZone] ID associated with the
    default time zone.
  * `name` is a short name associated with the default time
    zone.
  * `description` is a long name associated with the default
    time zone.
  * `utc.offset` is the historically correct XML duration added
    to UTC time to get local time in the default time zone for
    the given `$datetime` instant. If daylight savings time is in
    effect for the given `$datetime` instant, then this value was
    adjusted to include the daylight savings offset.
  * `dst.used?` is a boolean indicating if daylight savings
    time is used by this time zone.
  * `dst.active?` is a boolean indicating if daylight savings
    time is in effect for the given `$datetime` instant.
  * `dst.offset` is the XML duration added to the UTC offset
    when daylight savings time is in effect.

---

### tundra.uri:decode

Decodes a URI-encoded (application/x-www-form-urlencoded) string, according
to [RFC 2396].

The following rules are applied in the conversion:

* The alphanumeric characters "a" through "z", "A" through "Z" and "0"
through "9" remain the same.
* The special characters ".", "-", "*", and "_" remain the same.
* The plus sign "+" is converted into a space character " " .
* A sequence of the form "%xy" will be treated as representing a byte
where xy is the two-digit hexadecimal representation of the 8 bits. Then,
all substrings that contain one or more of these byte sequences
consecutively will be replaced by the character(s) whose encoding would
result in those consecutive bytes. The encoding scheme used to decode
these characters may be specified, or if unspecified, the default
encoding of the platform will be used.

Implemented with the [java.net.URLDecoder] class.

#### Inputs:

* `$string` is a string containing URI-encoded data to be decoded.
* `$encoding` is the character set used to determine what characters are
  represented by any consecutive sequences of the form "%xy". Defaults
  to [UTF-8].

#### Outputs:

* `$string` is the decoded input string.

---

### tundra.uri:emit

Emits a Uniform Resource Identifier ([URI]) string, according to
[RFC 2396], given its constituent parts.

URIs can be categorized as either hierarchical, where the scheme and body
parts are separated by the character sequence '://', or opaque, where the
scheme and body parts are separated by a ':' character.

Examples of hierarchical URIs:
* http://example.com/
* ftp://example.com/path/file.txt

Examples of opaque URIs:
* mailto:john.doe@example.com
* news:comp.lang.java
* urn:isbn:096139210x

Opaque URIs are constructed according to the following syntax:

    (scheme:)body(?query)(#fragment)

Where brackets (...) delineate optional components and the characters
':', '?', and '#' stand for themselves.

Hierarchical URIs are constructed according to the following syntax:

    (scheme:)(//authority)(path)(?query)(#fragment)

Where the characters ':', '/', '?', and '#' stand for themselves. The
authority component, if specified, can be either server-based or registry-
based. If server-based, it is constructed according to the syntax:

    (user:password@)host(:port)

Implemented with the [java.net.URI] class.

#### Inputs:

* `$uri` is an `IData` document containing the constituent parts to
  construct a new [URI] string with.

#### Outputs:

* `$string` is the resulting [URI] input string.

---

### tundra.uri:encode

URI encodes (application/x-www-form-urlencoded) a string, according
to [RFC 2396].

The following rules are applied in the conversion:

* The alphanumeric characters "a" through "z", "A" through "Z" and "0"
  through "9" remain the same.
* The special characters ".", "-", "*", and "_" remain the same.
* The plus sign "+" is converted into a space character " " .
* A sequence of the form "%xy" will be treated as representing a byte
  where xy is the two-digit hexadecimal representation of the 8 bits. Then,
  all substrings that contain one or more of these byte sequences
  consecutively will be replaced by the character(s) whose encoding would
  result in those consecutive bytes. The encoding scheme used to decode
  these characters may be specified, or if unspecified, the default
  encoding of the platform will be used.

Implemented with the [java.net.URLEncoder] class.

#### Inputs:

* `$string` is a string containing data to be URI-encoded.
* `$encoding` is the character set used to obtain the bytes for unsafe
  characters. Defaults to [UTF-8].

#### Outputs:

* `$string` is the URI-encoded input string.

---

### tundra.uri:normalize

Normalizes a URI string.

Refer to the following excerpt from the [java.net.URI normalize] method for
a description of the normalization process:

> If this URI is opaque, or if its path is already in normal form, then this
> URI is returned. Otherwise a new URI is constructed that is identical to
> this URI except that its path is computed by normalizing this URI's path
> in a manner consistent with RFC 2396, section 5.2, step 6, sub-steps c
> through f; that is:
>
> 1. All "." segments are removed.
>
> 2. If a ".." segment is preceded by a non-".." segment then both of these
>    segments are removed. This step is repeated until it is no longer
>    applicable.
>
> 3. If the path is relative, and if its first segment contains a colon
>    character (':'), then a "." segment is prepended. This prevents a
>    relative URI with a path such as "a:b/c/d" from later being re-parsed
>    as an opaque URI with a scheme of "a" and a scheme-specific part of
>    "b/c/d". (Deviation from RFC 2396.)
>
> A normalized path will begin with one or more ".." segments if there were
> insufficient non-".." segments preceding them to allow their removal. A
> normalized path will begin with a "." segment if one was inserted by step
> 3 above. Otherwise, a normalized path will not contain any "." or ".."
> segments.

#### Inputs:

* `$string` is the [URI] to be normalized.

#### Outputs:

* `$string` is the given [URI] in normalized form.

---

### tundra.uri:parse

Parses a Uniform Resource Identifier ([URI]) string, according to
[RFC 2396], to its constituent parts.

URIs can be categorized as either hierarchical, where the scheme and body
parts are separated by the character sequence '://', or opaque, where the
scheme and body parts are separated by a ':' character.

Examples of hierarchical URIs:

* http://example.com/
* ftp://example.com/path/file.txt

Examples of opaque URIs:

* mailto:john.doe@example.com
* news:comp.lang.java
* urn:isbn:096139210x

Opaque URIs are constructed according to the following syntax:

    (scheme:)body(?query)(#fragment)

Where brackets (...) delineate optional components and the characters
':', '?', and '#' stand for themselves.

Hierarchical URIs are constructed according to the following syntax:

    (scheme:)(//authority)(path)(?query)(#fragment)

Where the characters ':', '/', '?', and '#' stand for themselves. The
authority component, if specified, can be either server-based or registry-
based. If server-based, it is constructed according to the syntax:

    (user:password@)host(:port)

Where the characters '@' and ':' stand for themselves.

Opaque URI bodies are not subject to further parsing, however, to
accomodate the mailto: URI's use of a query string for additional data
(such as cc, bcc, subject, and body), this service checks if the body
contains a query string and, if so, parses the query string also.

Implemented with the [java.net.URI] class.

#### Inputs:

* `$string` is the a [URI] string to be parsed.

#### Outputs:

* `$uri` is an `IData` document containing the constituent parts of the
  parsed [URI] string.

---

### tundra.user:current

Returns the current Integration Server user used to invoke this service.

#### Outputs:

* `$user` is the currently logged on user name that invoked this service.

---

### tundra.xml:canonicalize

Canonicalizes the given XML content using the given algorithm.
Implemented with the [Apache Santuario] XML Security library.

#### Inputs:

* `$content` is the XML content to be canonicalized, specified as a
  string, byte array, or input stream.
* `$encoding` is an optional character set to use when `$content` is
  provided as a byte array or input stream to decode the contained
  text data. Defaults to [UTF-8].
* `$algorithm` determines the canonicalization algorithm used, and
  is a choice of:
  * [Canonical XML Version 1.0]
  * [Canonical XML Version 1.0 With Comments]
  * [Canonical XML Version 1.1]
  * [Canonical XML Version 1.1 With Comments]
  * [Exclusive Canonical XML Version 1.0]
  * [Exclusive Canonical XML Version 1.0 With Comments]
* `$mode` is a choice of bytes, stream, or string, and determines
  the type of object `$content.canonical` is returned as. Defaults
  to stream.

#### Outputs:

* `$content.canonical` is the given `$content` converted to canonical
  form by the given `$algorithm`.

---

### tundra.xml.mime.type:check

Returns true if the given MIME media type is recognized as a [XML] media
type.

#### Inputs:

* `$content.type` is the MIME media type to be checked.

#### Outputs:

* `$xml?` is a boolean which when true indicates that the given
  `$content.type` is a recognized [XML] media type.

---

### tundra.xml:validate

Validates the given content as [XML], and optionally against an [XML]
schema ([XSD]).

Uses the Simple API for XML ([SAX]) algorithm for parsing which, as it is
event-based, is not memory-constrained and can handle arbitrarily large
documents when parsing from an input stream.

#### Inputs:

* `$content` is a string, byte array, or input stream potentially
  containing [XML] data.
* `$content.encoding` is the character set used by `$content` if
  provided as a byte array or input stream. Defaults to [UTF-8].
* `$schema` is a string, byte array, or input stream containing [XSD]
  data.
* `$schema.encoding` is the character set used by `$schema` if provided
  as a byte array or input stream. Defaults to [UTF-8].
* `$raise?` is an optional boolean flag, if true and `$content` is
  malformed or invalid an exception will be thrown. If false, no
  exception will be thrown. Defaults to false.

#### Outputs:

* `$valid?` is true if the given `$content` is well-formed [XML] and, if
  a `$schema` was specified, valid when compared to the given `$schema`.
* `$errors` is an optional list of the errors detected by the parser in
  the given `$content`, provided when `$valid?` is false.

---

### tundra.xpath:exists

Returns true if the given XPath expression `$expression` exists the given
XML `$content`.

#### Inputs:

* `$content` is a string, byte array, or input stream containing XML
  data.
* `$encoding` is the character set used by `$content` if provided as a
  byte array or input stream. Defaults to [UTF-8].
* `$expression` is the [XPath expression] to be tested against
  `$content`.

#### Outputs:

* `$exists?` is true if the XPath expression was found to exist in the
  given `$content`.

---

### tundra.yaml:emit

Serializes an IData document as a [YAML] formatted string, byte array, or
input stream.

#### Inputs:

* `$document` is the IData document to be serialized as a [YAML] string,
  byte array, or input stream.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to [UTF-8].
* `$mode` is an optional choice of stream, bytes, or string which
  specifies the type of object `$content` is returned as. Defaults to
  stream.

#### Outputs:

* `$content` is the resulting serialization of `$document` as [YAML]
  content.

---

### tundra.yaml.mime.type:check

Returns true if the given MIME media type is recognized as a [YAML] media
type.

#### Inputs:

* `$content.type` is the MIME media type to be checked.

#### Outputs:

* `$yaml?` is a boolean which when true indicates that the given
  `$content.type` is a recognized [YAML] media type.

---

### tundra.yaml:parse

Parses [YAML] content specified as a string, byte array, or input stream
into an IData document.

#### Inputs:

* `$content` is a string, byte array, or input stream containing [YAML]
  content to be parsed.
* `$encoding` is an optional character set to use when `$content` is
  provided as a byte array or input stream to decode the contained text
  data. Defaults to [UTF-8].

#### Outputs:

* `$document` is the resulting IData document representing the parsed
  `$content`.

---

### tundra.zip:compress

Compresses the given contents using the [zip] file compression format.

#### Inputs:

* `$contents` is an IData document list containing the data to be
  compressed.
  * `name` is the file path and name given to this item in the
    resulting zip archive.
  * `content` is the data to be compressed, specified as either a
    string, byte array, or input stream.
  * `encoding` is an optional character set used when `content` is
    specified as a string. Defaults to [UTF-8].
* `$mode` is an optional choice of 'stream', 'bytes', or
  'string', which determines the type of object returned by
  this service. If the 'string' mode is chosen, the resulting
  zipped data is base64-encoded. Defaults to 'stream'.

#### Outputs:

* `$contents.zip` is the resulting compressed data in [zip] format.

---

### tundra.zip:decompress

Decompresses the given content using the [zip] file compression format.

#### Inputs:

* `$contents.zip` is the [zip] compressed data to be decompressed,
  specified as a base64-encoded string, byte array, or input stream.
* `$encoding` is an optional character set used to decode the
  decompressed data when the chosen `$mode` is 'string'. Defaults to
  [UTF-8].
* `$mode` is an optional choice of 'stream', 'bytes', or 'string',
  which determines the type of content returned by this service.
  Defaults to 'stream'.

#### Outputs:

* `$contents` is an IData document list containing the resulting
  decompressed data.
  * `name` is the file path and name assigned to this item in the
    zip archive.
  * `content` is the decompressed data associated with this item.

## Contributions

1. Check out the latest master to make sure the feature hasn't been
   implemented or the bug hasn't been fixed yet.
2. Check out the issue tracker to make sure someone already hasn't requested
   it and/or contributed it.
3. Fork the project.
4. Start a feature/bugfix branch.
5. Commit and push until you are happy with your contribution.
6. Make sure to add tests for it. This is important so it won't break in a
   future version unintentionally.

Please try not to mess with the package version, or history. If you want your
own version please isolate it to its own commit, so it can be cherry-picked
around.

## Copyright

Copyright &copy; 2012 Lachlan Dowding. See the [LICENSE] file for further details.

[Apache Santuario]: <http://santuario.apache.org/>
[Base64]: <http://en.wikipedia.org/wiki/Base64>
[Canonical XML Version 1.0]: <http://www.w3.org/TR/2001/REC-xml-c14n-20010315>
[Canonical XML Version 1.0 With Comments]: <http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments>
[Canonical XML Version 1.1]: <http://www.w3.org/2006/12/xml-c14n11>
[Canonical XML Version 1.1 With Comments]: <http://www.w3.org/2006/12/xml-c14n11#WithComments>
[capturing group]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html#cg>
[capturing groups]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html#cg>
[catch block]: <http://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html>
[CSV]: <http://en.wikipedia.org/wiki/Comma-separated_values>
[default charset]: <http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html#defaultCharset()>
[default locale]: <http://docs.oracle.com/javase/6/docs/api/java/util/Locale.html#getDefault()>
[event loop]: <http://en.wikipedia.org/wiki/Event_loop>
[Exclusive Canonical XML Version 1.0]: <http://www.w3.org/2001/10/xml-exc-c14n#>
[Exclusive Canonical XML Version 1.0 With Comments]: <http://www.w3.org/2001/10/xml-exc-c14n#WithComments>
[finally block]: <http://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html>
[gzip]: <http://en.wikipedia.org/wiki/Gzip>
[HTML]: <http://en.wikipedia.org/wiki/HTML>
[HTML entities]: <http://www.w3.org/TR/html4/sgml/entities.html>
[HTML entity]: <http://www.w3.org/TR/html4/sgml/entities.html>
[HTTP request method]: <http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html>
[HTTP response code]: <http://httpstatus.es/>
[HTTP status code]: <http://www.iana.org/assignments/http-status-codes/http-status-codes.txt>
[HTTP]: <http://tools.ietf.org/search/rfc2616>
[IData XML]: <http://documentation.softwareag.com/webmethods/wmsuites/wmsuite8-2_sp2/Integration_Server/8-2-SP1_Integration_Server_Java_API_Reference/com/wm/util/coder/IDataXMLCoder.html>
[ISO8601]: <http://en.wikipedia.org/wiki/ISO_8601>
[JAR]: <http://en.wikipedia.org/wiki/JAR_(file_format)>
[java.io.ByteArrayInputStream]: <http://docs.oracle.com/javase/6/docs/api/java/io/ByteArrayInputStream.html>
[java.io.InputStream]: <http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html>
[java.io.OutputStream]: <http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html>
[java.lang.String]: <http://docs.oracle.com/javase/6/docs/api/java/lang/String.html>
[java.lang.System.identityHashCode()]: <http://docs.oracle.com/javase/6/docs/api/java/lang/System.html#identityHashCode(java.lang.Object)>
[java.math.BigDecimal]: <http://docs.oracle.com/javase/6/docs/api/java/math/BigDecimal.html>
[java.math.BigDecimal grammar]: <http://docs.oracle.com/javase/6/docs/api/java/math/BigDecimal.html#BigDecimal(java.lang.String)>
[java.math.BigInteger]: <http://docs.oracle.com/javase/6/docs/api/java/math/BigInteger.html>
[java.net.URI]: <http://docs.oracle.com/javase/6/docs/api/java/net/URI.html>
[java.net.URI normalize]: <http://docs.oracle.com/javase/6/docs/api/java/net/URI.html#normalize()>
[java.net.URLDecoder]: <http://docs.oracle.com/javase/6/docs/api/java/net/URLDecoder.html>
[java.net.URLEncoder]: <http://docs.oracle.com/javase/6/docs/api/java/net/URLEncoder.html>
[java.text.SimpleDateFormat]: <http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html>
[java.util.Date]: <http://docs.oracle.com/javase/6/docs/api/java/util/Date.html>
[java.util.TimeZone]: <http://docs.oracle.com/javase/6/docs/api/java/util/TimeZone.html>
[java.util.regex.Pattern]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html>
[javax.activation.MimeType]: <http://docs.oracle.com/javase/6/docs/api/javax/activation/MimeType.html>
[javax.jms.BytesMessage]: <http://docs.oracle.com/javaee/1.4/api/javax/jms/BytesMessage.html>
[Java array class name]: <http://docs.oracle.com/javase/tutorial/reflect/special/arrayComponents.html>
[JMS]: <http://en.wikipedia.org/wiki/Java_Message_Service>
[JSON]: <http://www.json.org/>
[LICENSE]: <https://github.com/Permafrost/Tundra/blob/master/LICENSE>
[Locale]: <http://docs.oracle.com/javase/6/docs/api/java/util/Locale.html>
[map function]: <http://en.wikipedia.org/wiki/Map_(higher-order_function)>
[mime type]: <http://en.wikipedia.org/wiki/Internet_media_type>
[MIME]: <http://en.wikipedia.org/wiki/MIME>
[natural ordering]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Comparable.html>
[Object.toString()]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Object.html#toString()>
[primitive type]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Class.html#isPrimitive()>
[radix]: <http://en.wikipedia.org/wiki/Radix>
[regular expression pattern]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html>
[releases]: <https://github.com/Permafrost/Tundra/releases>
[RFC 2045]: <http://www.ietf.org/rfc/rfc2045.txt>
[RFC 2046]: <http://www.ietf.org/rfc/rfc2046.txt>
[RFC 2396]: <http://www.ietf.org/rfc/rfc2396.txt>
[RFC 4122]: <http://www.ietf.org/rfc/rfc4122.txt>
[rounding algorithm]: <http://docs.oracle.com/javase/6/docs/api/java/math/RoundingMode.html>
[SAX]: <http://en.wikipedia.org/wiki/Simple_API_for_XML>
[set intersection]: <http://en.wikipedia.org/wiki/Intersection_(set_theory)>
[SQL GROUP BY]: <http://en.wikipedia.org/wiki/SQL#Queries>
[try block]: <http://docs.oracle.com/javase/tutorial/essential/exceptions/try.html>
[TSV]: <http://en.wikipedia.org/wiki/Tab-separated_values>
[Tundra]: <https://github.com/Permafrost/Tundra>
[TundraTN]: <https://github.com/Permafrost/TundraTN>
[Tundra.java]: <https://github.com/Permafrost/Tundra.java>
[URI]: <http://www.w3.org/Addressing/>
[URI template]: <https://tools.ietf.org/html/rfc6570>
[UTF-8]: <http://en.wikipedia.org/wiki/UTF-8>
[UUID]: <http://docs.oracle.com/javase/6/docs/api/java/util/UUID.html>
[webMethods Integration Server]: <http://www.softwareag.com/corporate/products/wm/integration/products/ai/overview/default.asp>
[webMethods Trading Networks]: <http://www.softwareag.com/corporate/products/wm/integration/products/b2b/overview/default.asp>
[XML]: <http://www.w3.org/XML/>
[XPath expression]: <http://www.w3.org/TR/xpath/>
[XSD]: <http://www.w3.org/XML/Schema>
[YAML]: <http://www.yaml.org>
[zip]: <http://en.wikipedia.org/wiki/Zip_(file_format)>
