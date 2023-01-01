import React from "react";
import { render, screen } from "@testing-library/react";
import NotFound from "../screens/NotFound";
import '@testing-library/jest-dom/extend-expect';


test("renders Not Found copy", () => {
  
  render(
    
    <NotFound />
    
);

  expect(screen.getByText("Page Not Found!")).toBeInTheDocument();
});