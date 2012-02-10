package week4.generics;

public class GenericTest {
	
	public static void main(String[] args) {
		GenericClass<Animal> gca = new GenericClass<Animal>();
		GenericClass<Dog> gcd = new GenericClass<Dog>();
		GenericClass<Poodle> gcp = new GenericClass<Poodle>();
		GenericClass<Cat> gcc = new GenericClass<Cat>();
		
		testAll(gca);
		// testExtends(gca);  // The method testExtends(GenericClass<? extends Dog>) in the type GenericTest is not applicable for the arguments (GenericClass<Animal>)
		testSuper(gca);

		testAll(gcd);
		testExtends(gcd);
		testSuper(gcd);

		testAll(gcp);
		testExtends(gcp);
		// testSuper(gcp);  // The method testExtends(GenericClass<? extends Dog>) in the type GenericTest is not applicable for the arguments (GenericClass<Animal>)

		testAll(gcc);
		// testExtends(gcc);  // The method testExtends(GenericClass<? extends Dog>) in the type GenericTest is not applicable for the arguments (GenericClass<Animal>)
		// testSuper(gcc);    // The method testSuper(GenericClass<? super Dog>) in the type GenericTest is not applicable for the arguments (GenericClass<Cat>)

	}
	
	private static void testAll(GenericClass<?> gc_extends_object) {
		gc_extends_object.foo();
		Object o = gc_extends_object.getOne();
	}
	
	// Intuitively speaking, wildcards with supertype bounds let you write to a generic object, wildcards with subtype bounds let you read from a generic objects.
	
	// You can use the return values, but you can’t supply parameters to methods. 
	// Since we know the type extends Dog, anything we get back can be treated as a Dog (or any of its super classes).
	// Because the type extends Dog, the compiler cannot know which type must be provided, therefore none is allowed.
	private static void testExtends(GenericClass<? extends Dog> gc_extends_dog) {
		gc_extends_dog.foo();
		Dog dog = gc_extends_dog.getOne();
		// gc_extends_dog.setOne(dog); // The method setOne(capture#5-of ? extends Dog) in the type GenericClass<capture#5-of ? extends Dog> is not applicable for the arguments (Dog)
	}
	
	// You can supply parameters to methods, but you can’t use the return values. 
	private static void testSuper(GenericClass<? super Dog> gc_is_super_of_dog) {
		gc_is_super_of_dog.foo();
		Dog dog = new Dog("Fido");
		Poodle poodle = new Poodle("Foofie");
		gc_is_super_of_dog.setOne(dog);
		gc_is_super_of_dog.setOne(poodle);
		Object o = gc_is_super_of_dog.getOne();
		// dog = gc_is_super_of_dog.getOne();  // Type mismatch: cannot convert from capture#9-of ? super Dog to Dog
		// poodle = gc_is_super_of_dog.getOne();  // Type mismatch: cannot convert from capture#10-of ? super Dog to Poodle
	}
}

class GenericClass<T> {
	void foo() {
		System.out.println("foo");
	}
	private T one;
	T getOne() {
		return this.one;
	}
	void setOne(T one) {
		this.one = one;
	}
}


class Animal {
	public Animal(String type, String name) {
		this.setType(type);
		this.setName(type);
	}
	
	public Animal(String type) {
		this(type, "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	private String type;
	private String name;
}

class Dog extends Animal {
	public Dog(String name) {
		super("Dog", name);
	}
}

class Poodle extends Dog {
	public Poodle(String name) {
		super(name);
	}
}

class Cat extends Animal {
	public Cat(String name) {
		super("Cat", name);
	}
}


