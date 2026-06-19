interface PaginationControlsProps {
  limit: number;
  offset: number;
  total: number;
  onPageChange: (offset: number) => void;
  onLimitChange: (limit: number) => void;
  disabled: boolean;
}

export function PaginationControls({
  limit,
  offset,
  total,
  onPageChange,
  onLimitChange,
  disabled
}: PaginationControlsProps) {
  const currentPage = Math.floor(offset / limit) + 1;
  const totalPages = Math.max(1, Math.ceil(total / limit));
  const hasPrevious = offset > 0;
  const hasNext = offset + limit < total;

  return (
    <div className="pagination" aria-label="Pagination controls">
      <button
        type="button"
        disabled={disabled || !hasPrevious}
        onClick={() => onPageChange(Math.max(0, offset - limit))}
      >
        Previous
      </button>

      <span>
        Page {currentPage} of {totalPages}
      </span>

      <button
        type="button"
        disabled={disabled || !hasNext}
        onClick={() => onPageChange(offset + limit)}
      >
        Next
      </button>

      <label>
        Page size
        <select
          value={limit}
          disabled={disabled}
          onChange={(event) => onLimitChange(Number(event.target.value))}
        >
          <option value={5}>5</option>
          <option value={10}>10</option>
        </select>
      </label>
    </div>
  );
}
