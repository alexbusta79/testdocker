# Inditex Supplier Dashboard Frontend

React + TypeScript frontend for the Inditex Supplier Management challenge.

## Run locally

```bash
npm install
npm run dev
```

Create `.env` if needed:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Backend endpoint consumed:

```http
GET /suppliers/potential?rate={rate}&limit={limit}&offset={offset}
```

## Features

- Numeric rate input, minimum 250
- Results table with DUNS, name, country, annual turnover, rating, score
- Default score descending sort
- Loading, error and empty states
- Client-side search by supplier name or DUNS
- Sortable table headers asc/desc
- Country filter
- Rating filter A-E
- Pagination using backend `limit` and `offset`
- Result count using backend `pagination.total`
- Euro currency formatting with thousand separators
