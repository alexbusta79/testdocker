import type { PotentialSupplier, SortDirection, SortKey } from '../types/supplier';
import { formatEuro, formatScore } from '../utils/formatters';

interface SuppliersTableProps {
  suppliers: PotentialSupplier[];
  sortKey: SortKey;
  sortDirection: SortDirection;
  onSort: (key: SortKey) => void;
}

const columns: Array<{ key: SortKey; label: string; numeric?: boolean }> = [
  { key: 'duns', label: 'DUNS', numeric: true },
  { key: 'name', label: 'Name' },
  { key: 'country', label: 'Country' },
  { key: 'annualTurnover', label: 'Annual Turnover', numeric: true },
  { key: 'sustainabilityRating', label: 'Sustainability Rating' },
  { key: 'score', label: 'Score', numeric: true }
];

export function SuppliersTable({ suppliers, sortKey, sortDirection, onSort }: SuppliersTableProps) {
  const sortIndicator = (key: SortKey) => {
    if (key !== sortKey) return '↕';
    return sortDirection === 'asc' ? '↑' : '↓';
  };

  return (
    <div className="table-wrapper card">
      <table>
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.key} className={column.numeric ? 'numeric' : undefined}>
                <button
                  type="button"
                  className="sort-button"
                  onClick={() => onSort(column.key)}
                  aria-label={`Sort by ${column.label}`}
                >
                  {column.label} <span aria-hidden="true">{sortIndicator(column.key)}</span>
                </button>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {suppliers.map((supplier) => (
            <tr key={supplier.duns}>
              <td className="numeric">{supplier.duns}</td>
              <td>{supplier.name}</td>
              <td>{supplier.country}</td>
              <td className="numeric">{formatEuro(supplier.annualTurnover)}</td>
              <td>
                <span className={`rating rating-${supplier.sustainabilityRating}`}>
                  {supplier.sustainabilityRating}
                </span>
              </td>
              <td className="numeric score">{formatScore(supplier.score)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
