package webtoon.domains.post.input;

public enum EPostStatus {

	PUBLISHED("PUBLISHED"), DELETED("DELETED"), CRAFTED("CRAFTED");

	private String value;

	EPostStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static EPostStatus parse(String id) {
		EPostStatus right = null; // Default
		for (EPostStatus item : EPostStatus.values()) {
			if (item.getValue().equalsIgnoreCase(id)) {
				right = item;
				break;
			}
		}
		return right;
	}
}
