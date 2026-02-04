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
                    .build());
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
        if (formulas.size() >= 3) {
          zeus = formulas.get(0);
          poseidon = formulas.get(1);
          athena = formulas.get(2);
        }
      }

      // 3. Create Tours - AVEC DISTANCES
      Tour grece16 = null, grece13 = null, trail = null;
      if (tourRepository.count() == 0) {
        grece16 =
            tourRepository.save(
                Tour.builder()
                    .name("Grèce Evasion")
                    .country("Grèce")
                    .durationDays(16)
                    .distanceKm(3200) // ===== 16 jours × 200 km/jour
                    .description("Le grand tour de la Grèce")
                    .build());
        grece13 =
            tourRepository.save(
                Tour.builder()
                    .name("Grèce Classique")
                    .country("Grèce")
                    .durationDays(13)
                    .distanceKm(2600) // ===== 13 jours × 200 km/jour
                    .description("L'essentiel de la Grèce")
                    .build());
        tourRepository.save(
            Tour.builder()
                .name("Crète Sauvage")
                .country("Grèce")
                .durationDays(9)
                .distanceKm(1800)
                .description("Découverte de la Crète")
                .build());
        trail =
            tourRepository.save(
                Tour.builder()
                    .name("Trail Explorer")
                    .country("Grèce")
                    .durationDays(7)
                    .distanceKm(1200) // ===== Moins de km/jour pour off-road
                    .description("Off-road adventure")
                    .build());
        System.out.println("Tours created with distances.");
      } else {
        // Récupérer les tours existants pour la section 4
        List<Tour> tours = tourRepository.findAll();
        if (tours.size() >= 4) {
          grece16 = tours.get(0);
          grece13 = tours.get(1);
          trail = tours.get(3);
        }
      }

      // ===== 4. Link Tours & Formulas (TourFormula) - SECTION MANQUANTE =====
      if (tourFormulaRepository.count() == 0 && grece16 != null && zeus != null) {
        // Grèce 16j: Pas de Zeus (selon cahier des charges)
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece16).formula(poseidon).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece16).formula(athena).isActive(true).build());

        // Grèce 13j: Toutes les formules
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(zeus).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(poseidon).isActive(true).build());
        tourFormulaRepository.save(
            TourFormula.builder().tour(grece13).formula(athena).isActive(true).build());

        // Trail: Athena uniquement (selon cahier des charges)
        tourFormulaRepository.save(
            TourFormula.builder().tour(trail).formula(athena).isActive(true).build());

        System.out.println("TourFormulas linked (restrictions applied).");
      }

      // ===== 5. Moto Categories & Locations - PRIX EN CENTIMES =====
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

        // Prices - EN CENTIMES (multiplier par 100)
        LocalDate today = LocalDate.now();

        // ROADSTER : 80€/jour = 8000 centimes, 0.50€/km = 50 centimes
        motoCategoryPriceRepository.save(
            MotoCategoryPrice.builder()
                .motoCategory(roadster)
                .country("Grèce")
                .dailyPrice(new BigDecimal("8000"))
                .kmPrice(new BigDecimal("50"))
                .startDate(today)
                .endDate(today.plusYears(1))
                .build());

        // TRAIL : 100€/jour = 10000 centimes, 0.60€/km = 60 centimes
        motoCategoryPriceRepository.save(
            MotoCategoryPrice.builder()
                .motoCategory(trailCat)
                .country("Grèce")
                .dailyPrice(new BigDecimal("10000"))
                .kmPrice(new BigDecimal("60"))
                .startDate(today)
                .endDate(today.plusYears(1))
                .build());

        // TOURING : 120€/jour = 12000 centimes, 0.70€/km = 70 centimes
        motoCategoryPriceRepository.save(
            MotoCategoryPrice.builder()
                .motoCategory(touring)
                .country("Grèce")
                .dailyPrice(new BigDecimal("12000"))
                .kmPrice(new BigDecimal("70"))
                .startDate(today)
                .endDate(today.plusYears(1))
                .build());

        System.out.println("Moto prices created in centimes (daily + km pricing).");
      }

      // 6. Options - PRIX EN CENTIMES
      if (optionRepository.count() == 0) {
        optionRepository.saveAll(
            Arrays.asList(
                Option.builder()
                    .name("Réservation de vol")
                    .price(new BigDecimal("25000"))
                    .targetType("VOL")
                    .build(),
                Option.builder()
                    .name("Transfert Aéroport")
                    .price(new BigDecimal("5000"))
                    .targetType("TRANSFER")
                    .build(),
                Option.builder()
                    .name("Petit-déjeuner")
                    .price(new BigDecimal("1500"))
                    .targetType("REPAS")
                    .build()));
        System.out.println("Options created in centimes.");
      }

      // 7. Accommodations Prices - PRIX EN CENTIMES
      if (accommodationRepository.count() == 0) {
        Accommodation hotel1 =
            accommodationRepository.save(
                Accommodation.builder()
                    .name("Hôtel Athènes Grand Comfort")
                    .city("Athènes")
                    .country("Grèce")
                    .type("HOTEL")
                    .build());

        LocalDate today = LocalDate.now();

        // SINGLE : 120€/nuit = 12000 centimes
        accommodationPriceRepository.save(
            AccommodationPrice.builder()
                .accommodationId(hotel1.getId())
                .startDate(today)
                .endDate(today.plusYears(1))
                .nightlyPrice(new BigDecimal("12000"))
                .roomType("SINGLE")
                .build());

        // COUPLE : 150€/nuit = 15000 centimes
        accommodationPriceRepository.save(
            AccommodationPrice.builder()
                .accommodationId(hotel1.getId())
                .startDate(today)
                .endDate(today.plusYears(1))
                .nightlyPrice(new BigDecimal("15000"))
                .roomType("COUPLE")
                .build());

        // SHARED : 80€/nuit = 8000 centimes
        accommodationPriceRepository.save(
            AccommodationPrice.builder()
                .accommodationId(hotel1.getId())
                .startDate(today)
                .endDate(today.plusYears(1))
                .nightlyPrice(new BigDecimal("8000"))
                .roomType("SHARED")
                .build());

        System.out.println("Accommodation prices created in centimes.");
      }

      // 8. Tour Prices - PRIX EN CENTIMES
      if (tourPriceRepository.count() == 0 && grece13 != null) {
        // Récupérer les TourFormulas pour Grèce 13 jours
        List<TourFormula> grece13Formulas =
            tourFormulaRepository.findByTourIdAndIsActiveTrue(grece13.getId());

        for (TourFormula tf : grece13Formulas) {
          String formulaName = tf.getFormula().getName();
          BigDecimal basePrice;

          // Prix différents selon la formule (en centimes)
          switch (formulaName) {
            case "Zeus":
              basePrice = new BigDecimal("180000"); // 1800€
              break;
            case "Poseidon":
              basePrice = new BigDecimal("150000"); // 1500€
              break;
            case "Athena":
              basePrice = new BigDecimal("120000"); // 1200€
              break;
            default:
              basePrice = new BigDecimal("150000");
          }

          tourPriceRepository.save(
              TourPrice.builder()
                  .tourFormula(tf)
                  .startDate(LocalDate.of(2026, 1, 1))
                  .endDate(LocalDate.of(2026, 12, 31))
                  .basePrice(basePrice)
                  .build());
        }

        System.out.println("Tour prices created in centimes.");
      }
    };
  }
}
