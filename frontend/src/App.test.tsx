import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi, afterEach } from 'vitest';
import App from './App';

const mockResponse = {
  data: [
    {
      annualTurnover: 2100000,
      country: 'ES',
      duns: 444444442,
      name: 'ES 210 A',
      status: 'Active',
      sustainabilityRating: 'A',
      score: 262500
    },
    {
      annualTurnover: 2000000,
      country: 'ES',
      duns: 444444441,
      name: 'ES 200 A',
      status: 'Active',
      sustainabilityRating: 'A',
      score: 250000
    }
  ],
  pagination: { limit: 10, offset: 0, total: 2 }
};

afterEach(() => {
  vi.restoreAllMocks();
});

describe('App', () => {
  it('validates minimum rate', async () => {
    render(<App />);

    await userEvent.type(screen.getByLabelText(/order amount/i), '100');
    await userEvent.click(screen.getByRole('button', { name: /search/i }));

    expect(screen.getByText(/at least 250/i)).toBeInTheDocument();
  });

  it('loads and filters suppliers', async () => {
    vi.spyOn(globalThis, 'fetch').mockResolvedValue({
      ok: true,
      json: async () => mockResponse
    } as Response);

    render(<App />);

    await userEvent.type(screen.getByLabelText(/order amount/i), '1500000');
    await userEvent.click(screen.getByRole('button', { name: /search/i }));

    expect(await screen.findByText('ES 210 A')).toBeInTheDocument();
    expect(screen.getByText('262,500.00')).toBeInTheDocument();

    await userEvent.type(screen.getByLabelText(/search by name or duns/i), '444444441');

    expect(screen.queryByText('ES 210 A')).not.toBeInTheDocument();
    expect(screen.getByText('ES 200 A')).toBeInTheDocument();
  });
});
