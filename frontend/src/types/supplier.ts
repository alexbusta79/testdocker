export type SustainabilityRating = 'A' | 'B' | 'C' | 'D' | 'E';
export type SupplierStatus = 'Active' | 'Disqualified';

export interface PotentialSupplier {
  annualTurnover: number;
  country: string;
  duns: number;
  name: string;
  status: SupplierStatus;
  sustainabilityRating: SustainabilityRating;
  score: number;
}

export interface Pagination {
  limit: number;
  offset: number;
  total: number;
}

export interface PotentialSuppliersResponse {
  data: PotentialSupplier[];
  pagination: Pagination;
}

export type SortDirection = 'asc' | 'desc';
export type SortKey = keyof Pick<
  PotentialSupplier,
  'duns' | 'name' | 'country' | 'annualTurnover' | 'sustainabilityRating' | 'score'
>;
