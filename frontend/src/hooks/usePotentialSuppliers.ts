import { useCallback, useMemo, useState } from 'react';
import { fetchPotentialSuppliers } from '../api/supplierApi';
import type { Pagination, PotentialSupplier, SortDirection, SortKey, SustainabilityRating } from '../types/supplier';

const DEFAULT_PAGINATION: Pagination = { limit: 10, offset: 0, total: 0 };

function compareValues(a: unknown, b: unknown): number {
  if (typeof a === 'number' && typeof b === 'number') return a - b;
  return String(a).localeCompare(String(b), undefined, { numeric: true, sensitivity: 'base' });
}

export function usePotentialSuppliers() {
  const [rate, setRate] = useState<string>('');
  const [submittedRate, setSubmittedRate] = useState<number | null>(null);
  const [items, setItems] = useState<PotentialSupplier[]>([]);
  const [pagination, setPagination] = useState<Pagination>(DEFAULT_PAGINATION);
  const [limit, setLimit] = useState<number>(10);
  const [offset, setOffset] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCountry, setSelectedCountry] = useState('ALL');
  const [selectedRating, setSelectedRating] = useState<'ALL' | SustainabilityRating>('ALL');
  const [sortKey, setSortKey] = useState<SortKey>('score');
  const [sortDirection, setSortDirection] = useState<SortDirection>('desc');

  const load = useCallback(async (nextRate: number, nextLimit = limit, nextOffset = offset) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetchPotentialSuppliers({
        rate: nextRate,
        limit: nextLimit,
        offset: nextOffset
      });
      setItems(response.data ?? []);
      setPagination(response.pagination ?? { limit: nextLimit, offset: nextOffset, total: 0 });
      setSubmittedRate(nextRate);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unexpected error loading suppliers.');
      setItems([]);
      setPagination({ limit: nextLimit, offset: nextOffset, total: 0 });
    } finally {
      setLoading(false);
    }
  }, [limit, offset]);

  const submitSearch = useCallback(() => {
    const numericRate = Number(rate);
    if (!Number.isFinite(numericRate) || numericRate < 250) {
      setValidationError('The order amount must be at least 250 €.');
      return;
    }
    setValidationError(null);
    setOffset(0);
    void load(numericRate, limit, 0);
  }, [rate, limit, load]);

  const changePage = useCallback((nextOffset: number) => {
    if (submittedRate == null) return;
    setOffset(nextOffset);
    void load(submittedRate, limit, nextOffset);
  }, [submittedRate, limit, load]);

  const changeLimit = useCallback((nextLimit: number) => {
    setLimit(nextLimit);
    setOffset(0);
    if (submittedRate != null) void load(submittedRate, nextLimit, 0);
  }, [submittedRate, load]);

  const toggleSort = useCallback((key: SortKey) => {
    if (key === sortKey) {
      setSortDirection((current) => (current === 'asc' ? 'desc' : 'asc'));
      return;
    }
    setSortKey(key);
    setSortDirection(key === 'score' ? 'desc' : 'asc');
  }, [sortKey]);

  const countries = useMemo(
    () => Array.from(new Set(items.map((supplier) => supplier.country))).sort(),
    [items]
  );

  const visibleItems = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();

    return items
      .filter((supplier) => {
        const matchesSearch =
          normalizedSearch.length === 0 ||
          supplier.name.toLowerCase().includes(normalizedSearch) ||
          String(supplier.duns).includes(normalizedSearch);

        const matchesCountry = selectedCountry === 'ALL' || supplier.country === selectedCountry;
        const matchesRating = selectedRating === 'ALL' || supplier.sustainabilityRating === selectedRating;

        return matchesSearch && matchesCountry && matchesRating;
      })
      .sort((left, right) => {
        const result = compareValues(left[sortKey], right[sortKey]);
        return sortDirection === 'asc' ? result : -result;
      });
  }, [items, searchTerm, selectedCountry, selectedRating, sortKey, sortDirection]);

  return {
    rate,
    setRate,
    submitSearch,
    loading,
    error,
    validationError,
    pagination,
    limit,
    offset,
    changePage,
    changeLimit,
    searchTerm,
    setSearchTerm,
    selectedCountry,
    setSelectedCountry,
    selectedRating,
    setSelectedRating,
    sortKey,
    sortDirection,
    toggleSort,
    countries,
    visibleItems,
    rawItems: items,
    submittedRate
  };
}
