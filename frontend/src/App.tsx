import { FilterBar } from './components/FilterBar';
import { PaginationControls } from './components/PaginationControls';
import { SearchPanel } from './components/SearchPanel';
import { StatusMessage } from './components/StatusMessage';
import { SuppliersTable } from './components/SuppliersTable';
import { usePotentialSuppliers } from './hooks/usePotentialSuppliers';
import './styles.css';

export default function App() {
  const state = usePotentialSuppliers();

  const showInitial = state.submittedRate == null && !state.loading && !state.error;
  const showEmpty = state.submittedRate != null && !state.loading && !state.error && state.visibleItems.length === 0;
  const showTable = !state.loading && !state.error && state.visibleItems.length > 0;

  return (
    <main className="app-shell">
      <header className="hero">
        <p className="eyebrow">Inditex Supplier Management</p>
        <h1>Potential Suppliers Dashboard</h1>
        <p>
          Search eligible suppliers for an order amount, review their sustainability rating and compare their calculated score.
        </p>
      </header>

      <SearchPanel
        rate={state.rate}
        onRateChange={state.setRate}
        onSubmit={state.submitSearch}
        validationError={state.validationError}
        loading={state.loading}
      />

      <FilterBar
        searchTerm={state.searchTerm}
        onSearchTermChange={state.setSearchTerm}
        countries={state.countries}
        selectedCountry={state.selectedCountry}
        onCountryChange={state.setSelectedCountry}
        selectedRating={state.selectedRating}
        onRatingChange={state.setSelectedRating}
        total={state.pagination.total}
        visibleCount={state.visibleItems.length}
      />

      {showInitial && (
        <StatusMessage type="initial" message="Enter an order amount of at least 250 € to find potential suppliers." />
      )}

      {state.loading && <StatusMessage type="loading" message="Loading potential suppliers..." />}

      {state.error && <StatusMessage type="error" message={state.error} />}

      {showEmpty && <StatusMessage type="empty" message="No suppliers match the selected criteria." />}

      {showTable && (
        <>
          <SuppliersTable
            suppliers={state.visibleItems}
            sortKey={state.sortKey}
            sortDirection={state.sortDirection}
            onSort={state.toggleSort}
          />

          <PaginationControls
            limit={state.limit}
            offset={state.offset}
            total={state.pagination.total}
            onPageChange={state.changePage}
            onLimitChange={state.changeLimit}
            disabled={state.loading}
          />
        </>
      )}
    </main>
  );
}
