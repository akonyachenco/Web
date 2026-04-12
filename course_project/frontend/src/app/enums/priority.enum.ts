export enum PriorityEnum {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
}

export const PriorityLabels: Record<PriorityEnum, string> = {
  [PriorityEnum.LOW]: 'Низкий',
  [PriorityEnum.MEDIUM]: 'Средний',
  [PriorityEnum.HIGH]: 'Высокий'
}
