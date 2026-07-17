export interface GetProduct {
  id: number;
  name: string;
  description: string;
  price: number;
  status: boolean;
  discountPercentage: number;
  featured: boolean;
  categoryId: number;
  categoryName: string;
  principalImageUrl: string;
}