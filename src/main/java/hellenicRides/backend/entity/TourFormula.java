package hellenicRides.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tour_formulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourFormula {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tour_id", nullable = false)
  private Tour tour;

  @ManyToOne
  @JoinColumn(name = "formula_id", nullable = false)
  private Formula formula;

  @Column(name = "is_active")
  private Boolean isActive;
}
