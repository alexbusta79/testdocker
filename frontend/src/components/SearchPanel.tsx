interface SearchPanelProps {
  rate: string;
  onRateChange: (value: string) => void;
  onSubmit: () => void;
  validationError: string | null;
  loading: boolean;
}

export function SearchPanel({ rate, onRateChange, onSubmit, validationError, loading }: SearchPanelProps) {
  return (
    <section className="card search-card" aria-labelledby="search-title">
      <div>
        <h2 id="search-title">Potential suppliers</h2>
        <p>Find eligible suppliers for a given order amount.</p>
      </div>

      <div className="search-controls">
        <label htmlFor="rate-input">
          Order amount (€)
          <input
            id="rate-input"
            type="number"
            min={250}
            step={1}
            value={rate}
            placeholder="Minimum 250"
            onChange={(event) => onRateChange(event.target.value)}
            onKeyDown={(event) => {
              if (event.key === 'Enter') onSubmit();
            }}
            aria-invalid={Boolean(validationError)}
            aria-describedby={validationError ? 'rate-error' : undefined}
          />
        </label>

        <button type="button" onClick={onSubmit} disabled={loading}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </div>

      {validationError && (
        <p id="rate-error" className="validation-message" role="alert">
          {validationError}
        </p>
      )}
    </section>
  );
}
