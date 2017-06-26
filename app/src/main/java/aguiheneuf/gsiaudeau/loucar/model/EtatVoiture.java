package aguiheneuf.gsiaudeau.loucar.model;

public enum EtatVoiture {
	LOUE("loue"), DISPONIBLE("disponible"), NON_RENDU("non_rendu"), INDISPONIBLE("indisponible");

	private final String name;

	EtatVoiture(String s) {
		name = s;
	}

	@Override
	public String toString() {
		return name;
	}

}
