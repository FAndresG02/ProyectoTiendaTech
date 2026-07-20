import { GetImage } from "../image-interface/get-image";
import { GetReviews } from "../reviews-interface/get-reviews";
import { GetProductFeature } from "./get-product-feature";

export interface GetProductById {
  id: number;
  name: string;
  description: string;
  price: number;
  status: boolean;
  discountPercentage: number;
  featured: boolean;
  categoryId: number;
  categoryName: string;
  images: GetImage[];
  reviews: GetReviews[];
  averageRating: number;
  totalReviews: number;
  features: GetProductFeature[];
}