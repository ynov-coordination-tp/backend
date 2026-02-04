package hellenicRides.backend.service;

import hellenicRides.backend.entity.Option;
import hellenicRides.backend.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionService {

  private final OptionRepository optionRepository;

  public List<Option> getAllOptions() {
    return optionRepository.findAll();
  }

  public Optional<Option> getOptionById(Long id) {
    return optionRepository.findById(id);
  }

  public List<Option> getOptionsByTargetType(String targetType) {
    return optionRepository.findByTargetType(targetType);
  }

  public Option saveOption(Option option) {
    return optionRepository.save(option);
  }

  public Optional<Option> updateOption(Long id, Option details) {
    return optionRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(details.getName());
              existing.setPrice(details.getPrice());
              existing.setTargetType(details.getTargetType());
              return optionRepository.save(existing);
            });
  }

  public boolean deleteOption(Long id) {
    if (!optionRepository.existsById(id)) {
      return false;
    }
    optionRepository.deleteById(id);
    return true;
  }
}
