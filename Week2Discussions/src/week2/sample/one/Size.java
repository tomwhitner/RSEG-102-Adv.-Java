package week2.sample.one;

enum Size
 {
    SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");

    private Size(String abbreviation) { this.abbreviation = abbreviation; }
    public String getAbbreviation() { return abbreviation; }

    private String abbreviation;
    public boolean willFit(int waistSize) {
    	if (this == Size.SMALL) {
    		return (28 <= waistSize && waistSize < 32 );
    	}
    	if (this == Size.MEDIUM) {
    		return (32 <= waistSize && waistSize < 36 );
    	}
    	if (this == Size.LARGE) {
    		return (36 <= waistSize && waistSize < 40 );
    	}
    	if (this == Size.EXTRA_LARGE) {
    		return (40 <= waistSize && waistSize < 44 );
    	}
    	return false;
    }
 }