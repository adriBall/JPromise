# Java usage

[Make promises](#make)

[Class methods](#class)

[Instance methods](#instance)

[Chain example](#example)

<a name="make"/>

## Make promises
### Traditional way
```java
new Promise<String>(new PromiseTask() {
    public void execute() {
        // Inside this block you can call 'resolve(result)' or 'reject(anException)'
        ...
    }
})
```
### Alternative way
```java
new PromiseTask() {
    public void execute() {
        // Inside this block you can call 'resolve(result)' or 'reject(anException)'
        ...
    }
}.<String>promise() 
// With Java8 it can be just .promise() if the parameterized type can be inferred
```
`resolve` must be called with the appropriate type (in this example it is `String` or `Promise<String>` or `Promise<Promise<<String>>`... , promise unpacking is shown later).

<a name="class"/>

## Class methods
### resolve
```java
// Returns a Promise<Object> that resolves to 3 (as Object) or, in Java8, to other 
// parameterized type if some other is inferred
Promise.resolve(3)
Promise.<Integer>resolve(3) // returns a Promise<Integer> that resolves to 3
```
### reject
```java
// Returns a Promise<Object> that rejects to anException or, in Java8, to other 
// parameterized type if some other is inferred
Promise.reject(anException)
Promise.<Integer>reject(anException) // returns a Promise<Integer> that rejects to anException

```
### all
```java
// Returns a Promise<Iterable<Object>> or, in Java8, to other 
// parameterized type if some other is inferred.
// In the last case, the promises parameterized types must be homogeneous
Promise.all(Promise.resolve(1), Promise.resolve("aString")) 

// Returns a Promise<Iterable<Integer>>
Promise.<Integer>all(
    // They must be parameterized-type-homogeneous
    Promise.<Integer>resolve(1), 
    Promise.<Integer>resolve(2), 
    Promise.<Integer>resolve(3)
) 

// It also works with an Array or Collection of Promises
// but anyway it resolves to an Iterable
```
### race
```java
// Returns a Promise<Object> or, in Java8, to other 
// parameterized type if some other is inferred.
// In the last case, the promises parameterized types must be homogeneous
Promise.race(Promise.resolve(1), Promise.resolve("aString")) // As objects

// Returns a Promise<Integer>
Promise.<Integer>race (
    // They must be parameterized-type-homogeneous
    Promise.<Integer>resolve(1), 
    Promise.<Integer>resolve(2), 
    Promise.<Integer>resolve(3)
)

// It also works with an Array or Collection of Promises
```

<a name="instance"/>

## Instance methods
## then
Let p be a promise that resolves to an `Integer`. 

```java
p.then(new FulfillHandler<Integer>() {
    public void onFulfilled(Integer res) {
        // Receive the result, res
        // ... 
    }
}) // Returns a Promise<Integer> that resolves to the same res
```
```java
p.<String>then(new TransformHandler<Integer, String>() {
    public String transform(Integer res) {
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

```java
// It allows to handle any Exception that is thrown inside
// any promise execute block or passed through its reject method
p.unless(new RejectHandler() {
    public void onRejected(Exception e) {
        e.printStackTrace(); // Handle the Exception
    }
})
```

## then with RejectHadler
```java
p.then(fulfillHandler, rejectHandler)

// Is similar to

p.then(fulfillHandler).unless(rejectHandler)

```

```java
p.<String>then(transformHandler, rejectHandler)

// Is similar to

p.<String>then(transformHandler).unless(rejectHandler)

```

<a name="example"/>

## Chain example 
### Java8 syntax
```java
NumberService.requestNumber() // Suppose that this returns a Promise<Integer>
    .<Integer>then((Integer res) -> res + 1) // Result transform
    // Suppose that numberToString takes an Integer and returns a Promise<String>
	.<String>then(NumberToStringService::numberToString) // Promise unpacking
	.then(this::pickResult)
	.then(this::pickResultToo)
	.unless((e) -> e.printSackTrace()) // Exception case
```	

### Java7 syntax
```java
NumberService.requestNumber() // Suppose that this returns a Promise<Integer>
    // Result transform
    .<Integer>then(new TransformHandler<Integer,Integer>() {
        public Integer transform(Integer res) {
            return res + 1;
        }
    })
    // Suppose that numberToString takes an Integer and returns a Promise<String>
    // Promise unpacking
    .<String>then(new TransformHandler<Integer,String>() {
        public String transform(Integer res) {
            return NumberToStringService.numberToString(res);
        }
    }
    .then(new FulfillHandler<String>() {
        public void onFulfilled(String res) {
            pickResult(res);
        }
    })
    .then(new FulfillHandler<String>() {
        public void onFulfilled(String res) {
            pickResultToo(res);
        }
    })
    // Exception case
    .unless(new RejectHandler() {
        public void onRejected(Exception e) {
            e.printStackTrace();
        }
    })
```	