# JPromise
A promise implementation for Java7+ which I used in Android projects. It is compatible with Java7 and pleasant to use with Java8 and Kotlin. In contrast with some other implementations:
- It performs promise unpacking.
- Nicer reject/response syntax (in exchange that promise body can't be used as lambda expression).
- Balance between keeping the rigid type system (i.e. not treating all as objects) and comfortably allow promise unpacking and type transform when promise chaining.

## Usage guides

These guides assumes familiarity with promises. Otherwise, you might be interested in checking [this](https://www.promisejs.org) previously.

[Java](docs/javausage.md)

[Kotlin](docs/kotlinusage.md)

## Example with Java8
#### Promise chain
```java
numberService.requestNumber()
    .<Integer>then((Integer res) -> res + 1) // Transform
	.<String>then(toStringService::numberToString) // Transform via promise unpacking
	.then((String res) -> System.out.println(res)) // Pick result
	.then((String res) -> System.out.println("Again" + res)) // Pick result again
	.unless((Exception e) -> e.printStackTrace()) // Exception case
```	
#### Where
```java
interface NumberService { Promise<Integer> requestNumber(); };
interface ToStringService { Promise<String> numberToString(Integer n); };
```	
```java
NumberService numberService = new NumberService() {
    public Promise<Integer> requestNumber() {
        return new PromiseTask() {		
			public void execute() throws InterruptedException {
				Thread.sleep(2000L);
				resolve(2);
			}
		}.promise();
    }
}
```	
```java	
ToStringService toStringService = new ToStringService() {
    public Promise<String> numberToString(Integer n) {
		return new PromiseTask() {
			public void execute() throws InterruptedException {
				Thread.sleep(1000L);
				resolve("Number " + n);
			}
		}.promise();
    }
}
```	

## Same example with Kotlin
#### Promise chain
```kotlin
requestNumber()
	.then<Int>({res -> res + 1}) // Transform
	.then<String>(::numberToString) // Transform via promise unpacking
	.then(::println) // Pick result
	.then(::println) // Pick result again
	.unless(::println) // Exception case
```
#### Where
```kotlin
fun requestNumber(): Promise<Int> = object : PromiseTask() {
	override fun execute() {
		Thread.sleep(2000)
		resolve(2)
	}
}.promise()

fun numberToString(n: Int): Promise<String> = object : PromiseTask() {
	override fun execute() {
		Thread.sleep(1000)
		resolve("number: " + n.toString())
	}
}.promise()
```

## As dependency using JitPack

### Maven
Add the JitPack repository to your build file:

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Add the dependency:

```xml
<dependency>
    <groupId>com.github.adriBall</groupId>
    <artifactId>JPromise</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency:

```groovy
dependencies {
	implementation 'com.github.adriBall:JPromise:1.0.0'
}
```