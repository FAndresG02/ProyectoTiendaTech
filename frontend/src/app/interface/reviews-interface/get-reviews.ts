export interface GetReviews {
  userName: string;
  comment: string;
  rating: number;
  createdAt: string; // LocalDateTime se recibe como string ISO desde el backend
}