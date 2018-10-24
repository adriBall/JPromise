# Kotlin usage

[Make promises](#make)

[Class methods](#class)

[Instance methods](#instance)

[Chain example](#example)

<a name="make"/>

## Make promises
### Traditional way
```kotlin
Promise<String>(object : PromiseTask() {
    override fun execute() {
        // Inside this block you can call 'resolve(result)' or 'reject(anException)'
        ...
    }
})
```
### Alternative way
```kotlin
Promise<String> = object : PromiseTask() {
    override fun execute() {
        // Inside this block you can call 'resolve(result)' or 'reject(anException)'
        ...
    }
}.promise() // .promise<String>() is inferred due to the declared reference
```
`resolve` must be called with the appropriate type (in this example it is `String` or `Promise<String>` or `Promise<Promise<<String>>`... , promise unpacking is shown later).

<a name="class"/>

## Class methods
### resolve
```kotlin
// Returns a Promise<Object> that resolves to 3 (referenced as Object unless other parameterized type is inferred)
Promise.resolve(3) 
Promise.resolve<Integer>(3) // returns a Promise<Integer> that resolves to 3
```
### reject
```kotlin
// Returns a promise that rejects to anException (Promise<Object> unless other parameterized type is inferred)
Promise.reject(anException)
Promise.reject<Integer>(anException) // returns a Promise<Integer> that rejects to anException

```
### all
```kotlin
// Returns a Promise<Iterable<Object>> unless other parameterized type is inferred
Promise.all(Promise.resolve(1), Promise.resolve("aString")) 

// Returns a Promise<Iterable<Integer>>
Promise.all<Integer>(
    Promise.<Integer>resolve(1), 
    Promise.<Integer>resolve(2), 
    Promise.<Integer>resolve(3)
) // Must be type-homogeneous

// It also works with an Array or Collection of Promises
// but anyway it resolves to an Iterable
```
### race
```kotlin
// Returns a Promise<Object> unless other parameterized type is inferred
Promise.race(Promise.resolve(1), Promise.resolve("aString")) // As objects

// Returns a Promise<Integer>
Promise.race<Integer> (
    Promise.<Integer>resolve(1), 
    Promise.<Integer>resolve(2), 
    Promise.<Integer>resolve(3)
) // Must be type-homogeneous

// It also works with an Array or Collection of Promises
```

<a name="instance"/>

## Instance methods
## then
Let p be a promise that resolves to an `Int`. 

```kotlin
p.then(object : FulfillHandler<Int>() {
    override fun onFulfilled(res: Int) {
        // Receive the result, res
        // ... 
    }
}) // Returns a Promise<Int> that resolves to the same res
```
```kotlin
p.then<String>(object :  TransformHandler<Int, String>() {
    override fun transform(res: Int): String {
        // Receive the result, res,
        // and generates a new one of type String or
        // (here comes the promise unpacking)
        // a Promise<String> or a Promise<Promise<String>>
        // or a Promise<Promise<...<String>...>>
        // ... 
        return transformedRes
    }
}) // Returns a Promise<String> that resolves to a transformed result,
// waiting for the intermediate promises to be resolved in the promise unpacking case

```

## unless
Let p be a promise.

```kotlin
// It allows to handle any Exception that is thrown inside
// any promise execute block or passed through its reject method
p.unless(object : RejectHandler() {
    override fun onRejected(e: Exception) {
        e.printStackTrace(); // Handle the Exception
    }
})
```

## then with RejectHadler
```kotlin
p.then(fulfillHandler, rejectHandler)

// Is similar to

p.then(fulfillHandler).unless(rejectHandler)

```

```kotlin
p.then<String>(transformHandler, rejectHandler)

// Is similar to

p.then<String>(transformHandler).unless(rejectHandler)

```

<a name="example"/>

## Chain example
```kotlin
requestNumber() // Suppose that this returns a Promise<Int>
    .then<Int>(::increment) // Result transform
    // Suppose that numberToStringService takes an Int and returns a Promise<String>
    .then<String>(::numberToStringService) // Promise unpacking
    .then(::pickResult)
    .then(::pickResultToo)
    .unless(::handleException) // Exception case
```	 
