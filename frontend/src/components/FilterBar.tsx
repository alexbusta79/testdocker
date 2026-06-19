import type { SustainabilityRating } from '../types/supplier';

interface FilterBarProps {
  searchTerm: string;
  onSearchTermChange: (value: string) => void;
  countries: string[];
  selectedCountry: string;
  onCountryChange: (value: string) => void;
  selectedRating: 'ALL' | SustainabilityRating;
  onRatingChange: (value: 'ALL' | SustainabilityRating) => void;
  total: number;
  visibleCount: number;
}

const RATINGS: SustainabilityRating[] = ['A', 'B', 'C', 'D', 'E'];

export function FilterBar({
  searchTerm,
  onSearchTermChange,
  countries,
  selectedCountry,
  onCountryChange,
  selectedRating,
  onRatingChange,
  total,
  visibleCount
}: FilterBarProps) {
  return (
    <section className="card filters" aria-label="Supplier filters">
      <label>
        Search by name or DUNS
        <input
          type="search"
          value={searchTerm}
          placeholder="Type supplier name or DUNS"
          onChange={(event) => onSearchTermChange(event.target.value)}
        />
      </label>

      <label>
        Country
        <select value={selectedCountry} onChange={(event) => onCountryChange(event.target.value)}>
          <option value="ALL">All countries</option>
          {countries.map((country) => (
            <option key={country} value={country}>
              {country}
            </option>
          ))}
        </select>
      </label>

      <label>
        Sustainability rating
        <select
          value={selectedRating}
          onChange={(event) => onRatingChange(event.target.value as 'ALL' | SustainabilityRating)}
        >
          <option value="ALL">All ratings</option>
          {RATINGS.map((rating) => (
            <option key={rating} value={rating}>
              {rating}
            </option>
          ))}
        </select>
      </label>

      <div className="result-count" aria-live="polite">
        <strong>{total}</strong> total suppliers · <strong>{visibleCount}</strong> visible on this page
      </div>
    </section>
  );
}
