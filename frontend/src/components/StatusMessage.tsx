interface StatusMessageProps {
  type: 'loading' | 'error' | 'empty' | 'initial';
  message: string;
}

export function StatusMessage({ type, message }: StatusMessageProps) {
  return (
    <div className={`status-message ${type}`} role={type === 'error' ? 'alert' : 'status'}>
      {type === 'loading' && <span className="spinner" aria-hidden="true" />}
      <span>{message}</span>
    </div>
  );
}
