export interface GetProduct {
    id: number;
    name: string;
    description: string;
    price: number;
    picture: string;
    status: boolean;
    discountPercentage: number;
    featured: boolean;
    createdAt: string;
    categoryId: number;
    categoryName: string;
}
