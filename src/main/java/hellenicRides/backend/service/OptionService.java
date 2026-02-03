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
}
