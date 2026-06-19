import type { PotentialSuppliersResponse } from '../types/supplier';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export interface FetchPotentialSuppliersParams {
  rate: number;
  limit: number;
  offset: number;
}

export async function fetchPotentialSuppliers({
  rate,
  limit,
  offset
}: FetchPotentialSuppliersParams): Promise<PotentialSuppliersResponse> {
  const url = new URL('/suppliers/potential', API_BASE_URL);
  url.searchParams.set('rate', String(rate));
  url.searchParams.set('limit', String(limit));
  url.searchParams.set('offset', String(offset));

  const response = await fetch(url.toString(), {
    method: 'GET',
    headers: { Accept: 'application/json' }
  });

  if (!response.ok) {
    let message = 'Unable to load potential suppliers. Please try again.';
    try {
      const body = await response.json();
      if (body?.info) message = body.info;
    } catch {
      // Keep friendly fallback message.
    }
    throw new Error(message);
  }

  return response.json() as Promise<PotentialSuppliersResponse>;
}
