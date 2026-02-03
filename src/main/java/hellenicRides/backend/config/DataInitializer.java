package hellenicRides.backend.config;

import hellenicRides.backend.entity.*;
import hellenicRides.backend.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final OptionRepository optionRepository;
  private final AccommodationRepository accommodationRepository;
  private final AccommodationPriceRepository accommodationPriceRepository;
  private final CustomerRepository customerRepository;
  private final QuoteRepository quoteRepository;
  private final QuoteItemRepository quoteItemRepository;
  private final QuoteItemOptionRepository quoteItemOptionRepository;

  @Value("${app.admin.email:admin@test.com}")
  private String adminEmail;

  @Value("${app.admin.password:password}")
  private String adminPassword;

  @Bean
  public CommandLineRunner initData() {
    return args -> {
      // 1. Create Default Admin
      if (adminRepository.findByEmail(adminEmail) == null) {
        Admin admin =
            Admin.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .build();
        adminRepository.save(admin);
        System.out.println("Default Admin created: " + adminEmail);
      }

      // 2. Create Sample Options
      if (optionRepository.count() == 0) {
        optionRepository.saveAll(
            Arrays.asList(
                Option.builder()
                    .name("Réservation de vol")
                    .price(new BigDecimal("250.00"))
                    .targetType("VOL")
                    .build(),
                Option.builder()
                    .name("Transfert Aéroport")
                    .price(new BigDecimal("50.00"))
                    .targetType("TRANSFER")
                    .build(),
                Option.builder()
                    .name("Location Moto (Yamaha Ténéré 700)")
                    .price(new BigDecimal("120.00"))
                    .targetType("MOTO")
                    .build(),
                Option.builder()
                    .name("Assurance Véhicule Premium")
                    .price(new BigDecimal("30.00"))
                    .targetType("MOTO")
                    .build(),
                Option.builder()
                    .name("Petit-déjeuner")
                    .price(new BigDecimal("15.00"))
                    .targetType("REPAS")
                    .build(),
                Option.builder()
                    .name("Pension Complète")
                    .price(new BigDecimal("60.00"))
                    .targetType("REPAS")
                    .build()));
        System.out.println("Sample Options created.");
      }

      // 3. Create Sample Accommodations
      if (accommodationRepository.count() == 0) {
        Accommodation hotel1 =
            Accommodation.builder()
                .name("Hôtel Athènes Grand Comfort")
                .city("Athènes")
                .country("Grèce")
                .build();

        hotel1 = accommodationRepository.save(hotel1);

        Accommodation hotel2 =
            Accommodation.builder()
                .name("Villa Crète Moto")
                .city("Héraklion")
                .country("Grèce")
                .build();

        hotel2 = accommodationRepository.save(hotel2);

        // 4. Create Prices for Accommodations (Next 30 days)
        LocalDate today = LocalDate.now();
        String[] roomTypes = {"SINGLE", "COUPLE", "SHARED"};
        BigDecimal[] prices = {
          new BigDecimal("100.00"), new BigDecimal("150.00"), new BigDecimal("80.00")
        };

        for (int i = 0; i < 30; i++) {
          LocalDate date = today.plusDays(i);
          for (int j = 0; j < roomTypes.length; j++) {
            accommodationPriceRepository.save(
                AccommodationPrice.builder()
                    .accommodationId(hotel1.getId())
                    .roomType(roomTypes[j])
                    .nightlyPrice(prices[j])
                    .startDate(date)
                    .endDate(date.plusWeeks(1))
                    .build());

            accommodationPriceRepository.save(
                AccommodationPrice.builder()
                    .accommodationId(hotel2.getId())
                    .roomType(roomTypes[j])
                    .nightlyPrice(prices[j].subtract(new BigDecimal("20.00")))
                    .startDate(date)
                    .endDate(date.plusWeeks(1))
                    .build());
          }
        }
        System.out.println("Sample Accommodations and Prices created.");
      }

      // 5. Create Sample Customer, Quote, items, and item options
      if (customerRepository.count() == 0) {
        Customer customer =
            Customer.builder()
                .firstName("Jean")
                .lastName("Dupont")
                .email("jean.dupont@example.com")
                .phone("0102030405")
                .build();
        customer = customerRepository.save(customer);

        Quote quote =
            Quote.builder()
                .quoteNumber("QT-SAMPLE-001")
                .customerId(customer.getId())
                .tourPackageId(1L) // Assuming package 1 exists
                .departureDate(LocalDate.now().plusMonths(1))
                .returnDate(LocalDate.now().plusMonths(1).plusDays(10))
                .participantCount(1)
                .status("DRAFT")
                .lockedTotalPrice(new BigDecimal("1500.00"))
                .createdAt(LocalDateTime.now())
                .build();
        quote = quoteRepository.save(quote);

        QuoteItem item =
            QuoteItem.builder()
                .quoteId(quote.getId())
                .participantName("Jean Dupont")
                .motoLocationId(1L)
                .accommodationId(1L)
                .lockedUnitPrice(new BigDecimal("1500.00"))
                .build();
        item = quoteItemRepository.save(item);

        List<Option> allOptions = optionRepository.findAll();
        if (!allOptions.isEmpty()) {
          QuoteItemOption itemOption =
              QuoteItemOption.builder()
                  .quoteItemId(item.getId())
                  .optionId(allOptions.get(0).getId())
                  .quantity(1)
                  .lockedPrice(allOptions.get(0).getPrice())
                  .build();
          quoteItemOptionRepository.save(itemOption);
        }
        System.out.println("Sample Customer and Quote created.");
      }
    };
  }
}
