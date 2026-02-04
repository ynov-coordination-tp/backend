package hellenicRides.backend.service;

import hellenicRides.backend.dto.CustomerDto;
import hellenicRides.backend.entity.Customer;
import hellenicRides.backend.repository.CustomerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private final CustomerRepository customerRepository;

  /**
   * Trouve un client existant par email ou en crée un nouveau.
   *
   * @param customerDto Données du client
   * @return Le client existant ou nouvellement créé
   */
  @Transactional
  public Customer findOrCreateCustomer(CustomerDto customerDto) {
    validateCustomerData(customerDto);

    // Chercher d'abord par email
    Optional<Customer> existingCustomer = customerRepository.findByEmail(customerDto.getEmail());

    if (existingCustomer.isPresent()) {
      log.info("Client existant trouvé avec l'email: {}", customerDto.getEmail());
      Customer customer = existingCustomer.get();

      // Mettre à jour les infos si nécessaire
      updateCustomerIfNeeded(customer, customerDto);

      return customer;
    }

    // Sinon créer un nouveau client
    log.info("Création d'un nouveau client avec l'email: {}", customerDto.getEmail());
    return createNewCustomer(customerDto);
  }

  private void validateCustomerData(CustomerDto customerDto) {
    if (customerDto == null) {
      throw new IllegalArgumentException("Customer data is required");
    }

    if (customerDto.getEmail() == null || customerDto.getEmail().isBlank()) {
      throw new IllegalArgumentException("Customer email is required");
    }

    if (customerDto.getLastName() == null || customerDto.getLastName().isBlank()) {
      throw new IllegalArgumentException("Customer last name is required");
    }

    if (customerDto.getFirstName() == null || customerDto.getFirstName().isBlank()) {
      throw new IllegalArgumentException("Customer first name is required");
    }
  }

  private void updateCustomerIfNeeded(Customer customer, CustomerDto customerDto) {
    boolean hasChanges = false;

    // Mettre à jour le nom si différent
    if (!customer.getLastName().equals(customerDto.getLastName())) {
      customer.setLastName(customerDto.getLastName());
      hasChanges = true;
    }

    if (!customer.getFirstName().equals(customerDto.getFirstName())) {
      customer.setFirstName(customerDto.getFirstName());
      hasChanges = true;
    }

    // Mettre à jour le téléphone si fourni et différent
    if (customerDto.getPhone() != null && !customerDto.getPhone().equals(customer.getPhone())) {
      customer.setPhone(customerDto.getPhone());
      hasChanges = true;
    }

    if (hasChanges) {
      customerRepository.save(customer);
      log.info("Informations du client {} mises à jour", customer.getEmail());
    }
  }

  private Customer createNewCustomer(CustomerDto customerDto) {
    Customer customer =
        Customer.builder()
            .email(customerDto.getEmail())
            .lastName(customerDto.getLastName())
            .firstName(customerDto.getFirstName())
            .phone(customerDto.getPhone())
            .build();

    return customerRepository.save(customer);
  }

  /** Récupère un client par son ID. */
  public Optional<Customer> getCustomerById(Long id) {
    return customerRepository.findById(id);
  }
}
