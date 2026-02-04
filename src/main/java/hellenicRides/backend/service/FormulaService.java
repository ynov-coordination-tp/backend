package hellenicRides.backend.service;

import hellenicRides.backend.entity.Formula;
import hellenicRides.backend.repository.FormulaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormulaService {

  private final FormulaRepository formulaRepository;

  public List<Formula> getAllFormulas() {
    return formulaRepository.findAll();
  }

  public Optional<Formula> getFormulaById(Long id) {
    return formulaRepository.findById(id);
  }

  public Formula saveFormula(Formula formula) {
    return formulaRepository.save(formula);
  }

  public Optional<Formula> updateFormula(Long id, Formula details) {
    return formulaRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(details.getName());
              existing.setIncludesMoto(details.getIncludesMoto());
              existing.setIncludesAccommodation(details.getIncludesAccommodation());
              existing.setIncludesMeals(details.getIncludesMeals());
              return formulaRepository.save(existing);
            });
  }

  public boolean deleteFormula(Long id) {
    if (!formulaRepository.existsById(id)) {
      return false;
    }
    formulaRepository.deleteById(id);
    return true;
  }
}
