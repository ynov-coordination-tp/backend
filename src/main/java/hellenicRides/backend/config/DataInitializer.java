package hellenicRides.backend.config;

import hellenicRides.backend.entity.*;
import hellenicRides.backend.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

  // New Repositories
  private final TourRepository tourRepository;
  private final FormulaRepository formulaRepository;
  private final TourFormulaRepository tourFormulaRepository;
  private final TourPriceRepository tourPriceRepository;
  private final MotoCategoryRepository motoCategoryRepository;
  private final MotoCategoryPriceRepository motoCategoryPriceRepository;
  private final MotoLocationRepository motoLocationRepository;

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

      // 2. Create Formulas
      Formula zeus = null, poseidon = null, athena = null;
      if (formulaRepository.count() == 0) {
        zeus =
            formulaRepository.save(
                Formula.builder()
                    .name("Zeus")
                    .includesMoto(true)
                    .includesAccommodation(true)
                    .includesMeals(true)
                    .build());
        poseidon =
            formulaRepository.save(
                Formula.builder()
                    .name("Poseidon")
                    .includesMoto(false)
                    .includesAccommodation(true)
                    .includesMeals(false)
                    .build()); // Breakfast only logic
        // handled in business layer
        // or description
        athena =
            formulaRepository.save(
                Formula.builder()
                    .name("Athena")
                    .includesMoto(true)
                    .includesAccommodation(false)
                    .includesMeals(false)
                    .build());
        System.out.println("Formulas created.");
      } else {
        List<Formula> formulas = formulaRepository.findAll();
        // Simple lookup for demo purposes if already exists (unsafe for real prod but
        // ok for demo)
        if (formulas.size() >= 3) {
          zeus = formulas.get(0);
          poseidon = formulas.get(1);
          athena = formulas.get(2);
        }
      }

      // 3. Create Tours
      Tour grece16 = null, grece13 = null, trail = null;
      if (tourRepository.count() == 0) {
        grece16 =
            tourRepository.save(
                Tour.builder()
                    .name("Grèce Evasion")
                    .country("Grèce")
                    .durationDays(16)
                    .description("Le grand tour de la Grèce")
                    .build());
        grece13 =
            tourRepository.save(
                Tour.builder()
                    .name("Grèce Classique")
                    .country("Grèce")
                    .durationDays(13)
                    .description("L'essentiel de la Grèce")
                    .build());
        tourRepository.save(
            Tour.builder()
                .name("Crète Sauvage")
                .country("Grèce")
                .durationDays(9)
                .description("Découverte de la Crète")
                .build());
        trail =
            tourRepository.save(
                Tour.builder()
                    .name("Trail Explorer")
                    .country("Grèce")
                    .durationDays(7)
                    .description("Off-road adventure")
                    .build());
        System.out.println("Tours created.");
      }

      // 4. Link Tours & Formulas (TourFormula)
      if (tourFormulaRepository.count() == 0 && grece16 != null && zeus != null) {
        // Grèce 16j: Pas de Zeus
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece16).formula(poseidon).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece16).formula(athena).isActive(true).build());

        // Grèce 13j: Tout
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(zeus).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(poseidon).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(athena).isActive(true).build());

        // Trail: Athena only
        tourFormulaRepository.save(
            TourFormula.builder().tour(trail).formula(athena).isActive(true).build());

        System.out.println("TourFormulas linked.");
      }

      // 5. Moto Categories & Locations
      if (motoCategoryRepository.count() == 0) {
        MotoCategory roadster =
            motoCategoryRepository.save(MotoCategory.builder().name("Roadster").build());
        MotoCategory trailCat =
            motoCategoryRepository.save(MotoCategory.builder().name("Trail").build());
        MotoCategory touring =
            motoCategoryRepository.save(MotoCategory.builder().name("Touring").build());

        // Locations
        motoLocationRepository.save(
            MotoLocation.builder()
                .motoCategory(roadster)
                .brand("Yamaha")
                .model("MT-07")
                .count(10)
                .build());
        motoLocationRepository.save(
            MotoLocation.builder()
                .motoCategory(trailCat)
                .brand("Yamaha")
                .model("Ténéré 700")
                .count(5)
                .build());
        motoLocationRepository.save(
            MotoLocation.builder()
                .motoCategory(touring)
                .brand("BMW")
                .model("R1250GS")
                .count(3)
                .build());

        // Prices
        LocalDate today = LocalDate.now();
        motoCategoryPriceRepository.save(
            MotoCategoryPrice.builder()
                .motoCategory(roadster)
                .country("Grèce")
                .dailyPrice(new BigDecimal("80"))
                .startDate(today)
                .endDate(today.plusYears(1))
                .build());
        motoCategoryPriceRepository.save(
            MotoCategoryPrice.builder()
                .motoCategory(trailCat)
                .country("Grèce")
                .dailyPrice(new BigDecimal("100"))
                .startDate(today)
                .endDate(today.plusYears(1))
                .build());
      }

      // 6. Old Data Preservation (Accommodations, etc.)
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
                    .name("Petit-déjeuner")
                    .price(new BigDecimal("15.00"))
                    .targetType("REPAS")
                    .build()));
      }

      // Accommodations (Preserved logic)
      if (accommodationRepository.count() == 0) {
        Accommodation hotel1 =
            accommodationRepository.save(
                Accommodation.builder()
                    .name("Hôtel Athènes Grand Comfort")
                    .city("Athènes")
                    .country("Grèce")
                    .build());
        // Prices logic omitted for brevity but can be added back if essential for other
        // tests
      }
    };
  }
}
