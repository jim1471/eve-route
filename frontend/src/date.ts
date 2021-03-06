
export const dateFormat = (isoString: string): string => {
  return new Date(isoString)
    .toISOString().replace('T', ' ')
    .replace('.000Z', '')
    .slice(0, -3) +
    ' UTC';
};

export const dateAddDays = (isoString: string, days: number): string => {
  const result = new Date(isoString);
  result.setDate(result.getDate() + days);
  return result.toISOString();
};
