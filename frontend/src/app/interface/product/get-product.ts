export interface GetProduct {
    id: number;
    name: string;
    description: string;
    price: number;
    picture: string;
    status: boolean;  //neceario para mostrar solo los productos activos
    discountPercentage: number; //necesario para mostrar el porcentaje de descuento en la vista de productos
    featured: boolean; //necesario para mostrar los productos destacados en la vista de productos
    categoryId: number;
    categoryName: string;
}
