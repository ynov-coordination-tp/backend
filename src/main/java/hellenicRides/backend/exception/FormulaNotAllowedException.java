package hellenicRides.backend.exception;

import lombok.Getter;

@Getter
public class FormulaNotAllowedException extends RuntimeException {
  private final String formulaName;
  private final String tourName;
  private final String allowedFormulas;

  public FormulaNotAllowedException(String formulaName, String tourName, String allowedFormulas) {
    super(
        String.format(
            "La formule '%s' n'est pas disponible pour le circuit '%s'. Formules autorisées : %s",
            formulaName, tourName, allowedFormulas));
    this.formulaName = formulaName;
    this.tourName = tourName;
    this.allowedFormulas = allowedFormulas;
  }
}
